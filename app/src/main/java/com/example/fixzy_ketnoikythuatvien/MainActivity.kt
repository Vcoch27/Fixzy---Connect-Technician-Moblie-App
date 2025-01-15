package com.example.fixzy_ketnoikythuatvien

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import androidx.core.content.ContextCompat

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val editText = findViewById<EditText>(R.id.editTextSearch)
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_search)
        drawable?.setBounds(0, 0, 60, 60)
        editText.setCompoundDrawables(null, null, drawable, null)
    }
}