package com.dicoding.asclepius.view.result

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.local.AppDatabase
import com.dicoding.asclepius.local.Classification
import com.dicoding.asclepius.view.ResultViewModel
import com.dicoding.asclepius.view.ResultViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val appDao by lazy { database.appDao() }

    private val viewModel: ResultViewModel by viewModels {
        ResultViewModelFactory(appDao)
    }

    private var result: Classification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (intent.hasExtra(EXTRA_IMAGE_URI)) {
            val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
            imageUri?.let {
                Log.d(TAG, "Image URI received: $it")
                binding.resultImage.setImageURI(it)
                setupImageClassifier(it)
                imageClassifierHelper.classifyStaticImage(it)
            }
        } else if (intent.hasExtra(EXTRA_RESULT)) {
            result = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(EXTRA_RESULT, Classification::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_RESULT)
            }

            result?.let {
                updateView(Uri.parse(it.imageUri), it.label, it.score)
            }
        }
    }

    private fun setupImageClassifier(imageUri: Uri) {
        Log.d(TAG, "Setting up Image Classifier")
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = this
        )
    }

    private fun updateView(imageUri: Uri, label: String, score: Float) {
        Log.d(TAG, "Updating view with label: $label, score: $score")
        binding.resultImage.setImageURI(imageUri)
        binding.resultText.text =
            buildString {
                append(NumberFormat.getPercentInstance().format(score))
                append(" $label")
            }
        binding.resultText.setTextColor(
            if (label == "Cancer") getColor(R.color.red) else getColor(R.color.blue)
        )
    }

    override fun onError(error: String) {
        Log.e(TAG, "Error in Image Classification: $error")
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        Log.d(TAG, "Results received: $results")
        runOnUiThread {
            results?.let { classifications ->
                if (classifications.isNotEmpty() && classifications[0].categories.isNotEmpty()) {
                    classifications[0].categories.maxByOrNull { it.score }?.let {
                        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
                        updateView(imageUri, it.label, it.score)
                        result = Classification(
                            timestamp = System.currentTimeMillis(),
                            imageUri = imageUri.toString(),
                            label = it.label,
                            score = it.score
                        )
                        viewModel.insert(result!!)
                    }
                } else {
                    binding.resultText.text = ""
                }
            }
        }
    }

    companion object {
        private const val TAG = "ResultActivity"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}
