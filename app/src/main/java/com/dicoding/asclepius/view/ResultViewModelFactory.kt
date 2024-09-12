package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.local.AppDao

class ResultViewModelFactory(
    private val appDao: AppDao
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            ResultViewModel(appDao) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
