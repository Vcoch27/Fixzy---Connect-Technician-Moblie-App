package com.example.fixzy_ketnoikythuatvien.service.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body

data class RegisterProviderRequest(
    @SerializedName("userId") val userId: Int,
    @SerializedName("years_of_experience") val yearsOfExperience: Int,
    @SerializedName("bio") val bio: String,
    @SerializedName("certificate_url") val certificateUrl: String
)
data class RegisterProviderResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
)
data class RegistrationResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: Registration?,
    @SerializedName("error") val error: String?
)

data class Registration(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("years_of_experience") val yearsOfExperience: Int,
    @SerializedName("bio") val bio: String,
    @SerializedName("certificate_url") val certificateUrl: String,
    @SerializedName("status") val status: String?,
    @SerializedName("monthly_fee") val monthlyFee: String,
    @SerializedName("payment_trans_id") val paymentTransId: String?,
    @SerializedName("payment_status") val paymentStatus: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
data class CreatePaymentResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("order_url") val order_url: String?,
    @SerializedName("app_trans_id") val app_trans_id: String?,
    @SerializedName("error") val error: String?
)
data class CreatePaymentRequest(
    val userId: Int,
    val embedData: String
)
