package com.example.fixzy_ketnoikythuatvien.data.model

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("full_name") val name: String,
    val email: String,
    @SerializedName("firebase_uid") val uid: String,
    val phone: String? = null,
    val address: String? = null, // Thêm trường address
    @SerializedName("avatar_url") val avatarUrl: String? = null, // Thêm trường avatar_url
    val role: String? = "user" // Thêm trường role, mặc định là "user"
)