package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.local.AppDao
import com.dicoding.asclepius.local.Classification
import kotlinx.coroutines.launch

class ResultViewModel(
    private val appDao: AppDao
) : ViewModel() {

    fun insert(classification: Classification) {
        viewModelScope.launch {
            appDao.insert(classification)
        }
    }

    fun delete(classification: Classification) {
        viewModelScope.launch {
            appDao.delete(classification)
        }
    }
}
