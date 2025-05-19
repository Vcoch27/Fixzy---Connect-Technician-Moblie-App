package com.example.fixzy_ketnoikythuatvien.service

import com.example.fixzy_ketnoikythuatvien.BuildConfig
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.data.model.UserDataResponse
import com.example.fixzy_ketnoikythuatvien.service.model.AddScheduleRequest
import com.example.fixzy_ketnoikythuatvien.service.model.AddScheduleResponse
import com.example.fixzy_ketnoikythuatvien.service.model.AvailabilityResponse
import com.example.fixzy_ketnoikythuatvien.service.model.CategoryResponse
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingRequest
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingResponse
import com.example.fixzy_ketnoikythuatvien.service.model.CreatePaymentRequest
import com.example.fixzy_ketnoikythuatvien.service.model.CreatePaymentResponse
import com.example.fixzy_ketnoikythuatvien.service.model.CreateServiceRequest
import com.example.fixzy_ketnoikythuatvien.service.model.CreateServiceResponse
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.service.model.GetBookingsResponse
import com.example.fixzy_ketnoikythuatvien.service.model.GetModeServiceResponse
import com.example.fixzy_ketnoikythuatvien.service.model.GetNotificationsResponse
import com.example.fixzy_ketnoikythuatvien.service.model.GetProviderBookingsResponse
import com.example.fixzy_ketnoikythuatvien.service.model.GetServiceInformationResponse
import com.example.fixzy_ketnoikythuatvien.service.model.GetSummaryStatusResponse
import com.example.fixzy_ketnoikythuatvien.service.model.GoogleUserDataRequest
import com.example.fixzy_ketnoikythuatvien.service.model.ProviderResponse
import com.example.fixzy_ketnoikythuatvien.service.model.RegisterProviderRequest
import com.example.fixzy_ketnoikythuatvien.service.model.RegisterProviderResponse
import com.example.fixzy_ketnoikythuatvien.service.model.RegistrationResponse
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceResponse
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnicianResponse
import com.example.fixzy_ketnoikythuatvien.service.model.UpdateProfileRequest
import com.example.fixzy_ketnoikythuatvien.service.model.UpdateProfileResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class TestItem(val id: Int, val count: Int)

//BuildConfig.BASE_URL
object ApiClient {
    val apiService: ApiService by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}


interface ApiService {
    @GET("user/{userID}")
    fun getUserData(@Path("userID") firebase_uid: String): Call<UserDataResponse>

    @POST("user/sync-user")
    fun syncUser(@Body userData: UserData): Call<ResponseBody>

    @GET("api/test")
    fun getTestData(): Call<List<TestItem>>//đối tượng chứa danh sách các TestItem từ API
    //Call là interface đại diện cho một HTTP request vó thể thực thi, nó hoạt đôgn như một promise/futer

    @POST("user/auth")
    fun authenticate(@Header("Authorization") token: String): Call<UserData>

    @GET("/user/top-technicians")
    suspend fun getTopTechnicians(
        @Query("categoryID") categoryID: String? = null,
    ): TopTechnicianResponse

    @GET("/categories")
    fun getCategories(): Call<CategoryResponse>

    @GET("service/category/{categoryId}")
    suspend fun getServicesByCategory(@Path("categoryId") categoryId: Int): ServiceResponse

    @GET("user/provider/{providerId}")
    suspend fun getProviderDetails(@Path("providerId") providerId: Int): Response<ProviderResponse>

    @GET("/service/{serviceId}/availability")
    suspend fun getAvailability(@Path("serviceId") serviceId: Int): Response<AvailabilityResponse>

    @POST("bookings")
    @Headers("Content-Type: application/json")
     fun createBooking(
        @Header("Authorization") token: String,
        @Body body: CreateBookingRequest
    ): Call<CreateBookingResponse>

    @GET("bookings/user")
    fun getBookingsForUser(@Header("Authorization") token: String): Call<GetBookingsResponse>

    @PUT("bookings/{bookingId}/status")
    suspend fun updateBookingStatus(
        @Path("bookingId") bookingId: Int,
        @Body body: StatusUpdateRequest,
        @Query("rating") rating: Int? = null,
        @Query("feedback") feedback: String? = null,
        @Query("feedback_url") feedback_url: String? = null
    ): Response<StatusUpdateResponse>
    data class StatusUpdateRequest(val status: String,val user_id: Int,val role: String, val rating: Int? = null,val feedback: String? = null,val feedback_url: String? = null)
    data class StatusUpdateResponse(val success: Boolean, val message: String)

    @PUT("user/update")
    suspend fun updateUserProfile(
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    @POST("provider/register")
    fun registerProvider(
        @Body body: RegisterProviderRequest
    ): Call<RegisterProviderResponse>

    @GET("provider/registration")
    fun getRegistration(
        @Query("userId") userId: Int
    ): Call<RegistrationResponse>

    @POST("provider/{registrationId}/create-payment")
    fun createPayment(
        @Path("registrationId") registrationId: Int,
        @Body request: CreatePaymentRequest
    ): Call<CreatePaymentResponse>

    @POST("service")
    fun createService(
        @Header("Authorization") token: String,
        @Body body: CreateServiceRequest
    ): Call<CreateServiceResponse>

    @GET("service/provider-mode/{serviceId}")
    fun getModeService(
        @Header("Authorization") token: String,
        @Path("serviceId") serviceId: Int
    ): Call<GetModeServiceResponse>

    @GET("bookings/status-summary")
    fun getSummaryStatus(
        @Header("Authorization") token: String
    ):Call<GetSummaryStatusResponse>

    @POST("service/{serviceId}/schedules")
    fun addSchedule(
        @Header("Authorization") token: String,
        @Path("serviceId") serviceId: Int,
        @Body body: AddScheduleRequest
    ): Call<AddScheduleResponse>

    @POST("user/sync-google-user")
    fun syncGoogleUser(
        @Header("Authorization") token: String,
        @Body userData: GoogleUserDataRequest
    ): Call<ResponseBody>

    @GET("notifications/{userId}/all")
    fun getNotifications(
        @Path("userId") userId: Int
    ): Call<GetNotificationsResponse>

    @GET("bookings/provider/{providerId}")
    fun getProviderBookings(
        @Path("providerId") providerId: Int
    ): Call<GetProviderBookingsResponse>

    @GET("service/information/{serviceId}")
    fun getServiceInformation(
        @Path("serviceId") serviceId: Int
    ):Call<GetServiceInformationResponse>
}
