package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.data.remote.TestItem
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.CategoriesSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.GreetingSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.OffersSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.SearchBar
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.TopTechniciansSection
import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.TestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductHomePageScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    testViewModel: TestViewModel = viewModel()
) {
    val testData = testViewModel.testData.collectAsState().value
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 38.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column (
            modifier = Modifier.padding(top= 0.dp, start = 15.dp, end = 15.dp, bottom = 15.dp)
        ){
            GreetingSection()
            SearchBar()
            Scaffold(
                topBar = { TopAppBar(title = { Text("Test Data") }) }
            ) { padding ->
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(testData) { item ->
//                        TestItemView(item, testViewModel)
                    }
                }
            }
        }
        OffersSection()
        CategoriesSection()
        TopTechniciansSection()
    }
}
