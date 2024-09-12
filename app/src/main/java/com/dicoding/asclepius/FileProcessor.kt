package com.dicoding.asclepius

import android.content.Context
import java.io.FileOutputStream
import java.io.IOException

object FileProcessor {

    fun processFile(context: Context, inputFileName: String, outputFilePath: String) {
        try {
            context.assets.open(inputFileName).use { inputStream ->
                FileOutputStream(outputFilePath).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                }
            }
            println("File processing completed successfully.")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
