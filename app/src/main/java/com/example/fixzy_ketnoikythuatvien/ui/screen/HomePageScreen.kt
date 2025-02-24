package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.CategoriesSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.GreetingSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.OffersSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.SearchBar
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.TopTechniciansSection

@Composable
fun ProductHomePageScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column (
            modifier = Modifier.padding(top= 38.dp, start = 15.dp, end = 15.dp, bottom = 15.dp)
        ){
            GreetingSection()
            SearchBar()
        }
        OffersSection()
        CategoriesSection()
        TopTechniciansSection()
    }
}
