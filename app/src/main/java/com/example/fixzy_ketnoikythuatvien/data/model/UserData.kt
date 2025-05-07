package com.example.fixzy_ketnoikythuatvien.data.model

import android.util.Log
import com.google.gson.annotations.SerializedName
private const val TAG = "AUTH_SERVICE"
data class UserData(
    val id: Int? = null,
    @SerializedName("full_name") val name: String? = null,
    val email: String? = null,
    @SerializedName("firebase_uid") val firebase_uid: String? = null,
    val phone: String? = null,
    val address: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null,
    val role: String? = "user"
)

data class UserDataResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("user") val user: UserDataRaw?
) {
    data class UserDataRaw(
        @SerializedName("id") val id: Int? = null,
        @SerializedName("fullName") val fullName: String? = null,
        @SerializedName("email") val email: String?,
        @SerializedName("firebase_uid") val firebaseUid: String? = null,
        @SerializedName("phone") val phone: String? = null,
        @SerializedName("address") val address: String? = null,
        @SerializedName("avatarUrl") val avatarUrl: String? = null,
        @SerializedName("role") val role: String? = "user"
    )

    fun toUserData(): UserData {
        Log.d(TAG, "Starting toUserData conversion for user: $user")
        val userData = UserData(
            id = user?.id,
            name = user?.fullName,
            email = user?.email,
            firebase_uid = user?.firebaseUid,
            phone = user?.phone,
            address = user?.address,
            avatarUrl = user?.avatarUrl,
            role = user?.role ?: "user"
        )
        Log.d(TAG, "toUserData completed: $userData")
        return userData
    }
}