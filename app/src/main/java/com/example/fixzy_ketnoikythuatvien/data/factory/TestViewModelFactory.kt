package com.example.fixzy_ketnoikythuatvien.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fixzy_ketnoikythuatvien.data.remote.TestApiService
import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.TestViewModel

class TestViewModelFactory(private val api: TestApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TestViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
