package com.example.fixzy_ketnoikythuatvien

import androidx.compose.runtime.Composable
import android.app.Activity
import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowInsetsControllerCompat
import com.example.fixzy_ketnoikythuatvien.ui.navigation.AppNavigation

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
    // Hàm kiểm tra kết nối Firebase
    private fun testFirebaseConnection() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("test_connection")

        ref.setValue("Hello from Fixzy App!")
            .addOnSuccessListener {
                Log.d("FIREBASE_TEST", "✅ Gửi dữ liệu thành công!")
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE_TEST", "❌ Lỗi kết nối Firebase", e)
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Đảm bảo chế độ edge-to-edge nếu cần

        setContent {
            AppTheme {
                HideSystemUI()
                AppNavigation()
            }
        }
        testFirebaseConnection()
    }


}
