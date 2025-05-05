package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.CategoriesSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.GreetingSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.OffersSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.SearchBar
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.TopTechniciansSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductHomePageScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
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

        }
        OffersSection()
        CategoriesSection(navController = navController)
        TopTechniciansSection(navController = navController)
    }
}
