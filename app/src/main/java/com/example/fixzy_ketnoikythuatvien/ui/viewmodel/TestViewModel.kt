package com.example.fixzy_ketnoikythuatvien.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fixzy_ketnoikythuatvien.service.TestItem
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TestViewModel : ViewModel() {
    private val store = Store.store
    private val service = Service()
    private val TAG = "TestViewModel"

    private val _testData = MutableStateFlow<List<TestItem>>(emptyList())
    val testData: StateFlow<List<TestItem>> = _testData

    init {
        Log.d(TAG, "TestViewModel initialized")
        // ✅ Đăng ký lắng nghe store NGAY LẬP TỨC
        store.subscribe {
            val updatedData = store.state.test
            _testData.value = updatedData
            Log.d(TAG, "Test data updated from store: $updatedData")
        }

        // Gọi API
        loadData()
    }

    private fun loadData() {
        Log.d("appnav", "loadData called")
        viewModelScope.launch {
            try {
                service.getTest() // Không cần nhận về data, chỉ cần store cập nhật
            } catch (e: Exception) {
                Log.e(TAG, "Error loading data: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel cleared")
    }
}
