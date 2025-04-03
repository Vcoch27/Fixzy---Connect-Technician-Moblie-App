package com.example.fixzy_ketnoikythuatvien.data.model

data class SyncUserRequest(
    val firebase_uid: String,
    val email: String,
    val full_name: String?,
    val phone: String?
)
