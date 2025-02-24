package com.example.fixzy_ketnoikythuatvien

import androidx.compose.runtime.Composable
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.fixzy_ketnoikythuatvien.ui.components.ImageAdapter
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import kotlin.math.abs
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.fixzy_ketnoikythuatvien.data.model.OrderState
import com.example.fixzy_ketnoikythuatvien.ui.navigation.AppNavigation

import com.example.fixzy_ketnoikythuatvien.ui.screen.ProductDetailsScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun HideSystemUI() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        systemUiController.isSystemBarsVisible = false // Ẩn cả thanh trạng thái và thanh điều hướng
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Đảm bảo chế độ edge-to-edge nếu cần

        setContent {
            AppTheme {
                HideSystemUI()
                AppNavigation()
            }
        }
    }
}

