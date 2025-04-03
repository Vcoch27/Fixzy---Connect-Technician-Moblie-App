package com.example.fixzy_ketnoikythuatvien.ui.components.authComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun AuthCheckBox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text("I agree to the Terms of Service and Privacy Policy", fontSize = 12.sp)
    }
}