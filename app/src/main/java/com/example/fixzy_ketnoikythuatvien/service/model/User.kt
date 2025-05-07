package com.example.fixzy_ketnoikythuatvien.service.model

import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("firebase_uid") val firebaseUid: String,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("avatar_url") val avatarUrl: String?
)

data class UpdateProfileResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
)