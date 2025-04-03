package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fixzy_ketnoikythuatvien.data.factory.TestViewModelFactory
import com.example.fixzy_ketnoikythuatvien.data.remote.TestApiService
import com.example.fixzy_ketnoikythuatvien.data.remote.TestItem
import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.TestViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(api: TestApiService) {
    val viewModel: TestViewModel = viewModel(factory = TestViewModelFactory(api))
    val testData = viewModel.testData.collectAsState().value

    Log.d("TestScreen", "UI Received Data: $testData")  // ✅ Debugging

    Scaffold(
        topBar = { TopAppBar(title = { Text("Test Data") }) }
    ) { padding ->
        if (testData.isEmpty()) {
            Log.d("TestScreen", "No data found in UI")  // ✅ Log khi không có dữ liệu
            Text("No Data Found", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(testData) { item ->
                    TestItemView(item, viewModel)
                }
            }
        }
    }
}


@Composable
fun TestItemView(item: TestItem, viewModel: TestViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "ID: ${item.id} - Count: ${item.count}")

        Button(onClick = { viewModel.updateCount(item.id) }) {  // ✅ Gửi đúng `item.id`
            Text("Update Count")
        }
    }
}

