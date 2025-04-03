package com.example.fixzy_ketnoikythuatvien.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fixzy_ketnoikythuatvien.data.remote.RetrofitInstance
import com.example.fixzy_ketnoikythuatvien.data.remote.TestApiService
import com.example.fixzy_ketnoikythuatvien.data.remote.TestItem
import com.example.fixzy_ketnoikythuatvien.data.remote.UpdateCountRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
class TestViewModel(private val api: TestApiService) : ViewModel() {
    private val _testData = MutableStateFlow<List<TestItem>>(emptyList())
    val testData: StateFlow<List<TestItem>> = _testData

    init {
        fetchTestData()
    }

    private fun fetchTestData() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTestData()
                Log.d("TestViewModel", "Fetched Data in ViewModel: $response")  // ✅ Debugging
                _testData.value = response
            } catch (e: Exception) {
                Log.e("TestViewModel", "Error fetching data: ${e.message}", e)  // ❌ Error logs
            }
        }
    }

    fun updateCount(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.updateCount(UpdateCountRequest(id))
                if (response.success) {
                    Log.d("TestViewModel", "✅ Count updated successfully")
                } else {
                    Log.e("TestViewModel", "❌ Failed to update count!")
                }
            } catch (e: Exception) {
                Log.e("TestViewModel", "🔥 Error: ${e.message}", e)
            }
        }
    }
}
