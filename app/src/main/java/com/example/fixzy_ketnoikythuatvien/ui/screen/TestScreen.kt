package com.example.fixzy_ketnoikythuatvien.ui.screen
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.TestViewModel
@Composable
fun TestScreen(viewModel: TestViewModel) {
    val tests by viewModel.testData.collectAsState()
    Log.d("appnav", "Tests in UI: $tests")

    Column {
        if (tests.isEmpty()) {
            Text("No data available")
        } else {
            tests.forEach { test ->
                Text(test.toString())
            }
        }
    }
}
