// service/AuthService.kt
package com.example.fixzy_ketnoikythuatvien.service

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.data.model.UserDataResponse
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import org.json.JSONObject

class AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val store = Store.Companion.store
    private val TAG = "AUTH_SERVICE"

    private val apiService = ApiClient.apiService
    private var isFetchingUserData = false
    val providerService =  ProviderService()

    fun getUserData() {
        Log.d(TAG, "-------------getUserData called-----------")
        if (isFetchingUserData) {
            Log.d(TAG, "Skipping duplicate getUserData call")
            return
        }
        isFetchingUserData = true

        val firebaseUid = auth.currentUser?.uid
        if (firebaseUid.isNullOrEmpty()) {
            Log.e(TAG, "No Firebase UID found")
            store.dispatch(Action.FetchUserDataFailure("Không tìm thấy Firebase UID"))
            isFetchingUserData = false
            return
        }

        Log.e(TAG, "Fetching user data for UID: $firebaseUid")
        store.dispatch(Action.FetchUserDataStart(firebaseUid))
        Log.d(TAG, "Dispatch FetchUserDataStart completed")

        apiService.getUserData(firebaseUid).enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                isFetchingUserData = false
                Log.d(TAG, "GetUserData response received with code: ${response.code()}")

                try {
                    val userDataResponse = response.body()
                    Log.d(TAG, "Response body: $userDataResponse")

                    val rawJson = if (response.isSuccessful && userDataResponse != null) {
                        com.google.gson.Gson().toJson(userDataResponse)
                    } else {
                        response.errorBody()?.string() ?: "No error body available"
                    }

                    if (response.isSuccessful) {
                        if (userDataResponse?.success == true && userDataResponse.user != null) {
                            Log.d(TAG, "Checking role: ${userDataResponse.user.role}")
                            val validRoles = setOf("user", "technician")
                            val role = userDataResponse.user.role
                            if (role == null || role !in validRoles) {
                                Log.e(TAG, "Invalid or null role: $role")
                                store.dispatch(Action.FetchUserDataFailure("Vai trò không hợp lệ hoặc rỗng"))
                                return
                            }
                            val userData = userDataResponse.toUserData()
                            Log.d(TAG, "Dispatching FetchUserDataSuccess with UserData: $userData")
                            store.dispatch(Action.FetchUserDataSuccess(userData))
                            Log.d(TAG, "Dispatch FetchUserDataSuccess completed")
                        } else {
                            Log.w(TAG, "Response success=false or user null")
                            store.dispatch(Action.FetchUserDataFailure("Dữ liệu người dùng trống"))
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e(TAG, "Error body: $errorBody")
                        val errorMessage = try {
                            val errorJson = JSONObject(errorBody ?: "{}")
                            errorJson.getString("message") ?: "Lỗi: ${response.code()}"
                        } catch (e: Exception) {
                            "Lỗi khi lấy dữ liệu: ${response.code()}"
                        }
                        store.dispatch(Action.FetchUserDataFailure(errorMessage))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Crash in onResponse: ${e.message}", e)
                    store.dispatch(Action.FetchUserDataFailure("Lỗi xử lý dữ liệu: ${e.message ?: "Lỗi không xác định"}"))
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                isFetchingUserData = false
                Log.e(TAG, "GetUserData failed: ${t.message}", t)
                store.dispatch(Action.FetchUserDataFailure("Lỗi kết nối: ${t.message ?: "Không thể kết nối"}"))
            }
        })
    }
    fun signUp(
        email: String,
        password: String,
        name: String,
        phone: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.i(TAG, "Starting signUp with email: $email, name: $name, phone: $phone") // Log thông tin đầu vào
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Firebase sign-up successful") // Log khi Firebase Auth thành công
                    val user = auth.currentUser
                    if (user != null) {
                        Log.i(TAG, "Current user UID: ${user.uid}") // Log UID của người dùng
                        val userData = UserData(
                            name = name,
                            email = email,
                            firebase_uid = user.uid,
                            phone = phone,
                            address = null,
                            avatarUrl = null,
                            role = "user"
                        )
                        Log.i(TAG, "Saving userData to Firestore: $userData") // Log trước khi lưu vào Firestore
                        firestore.collection("users")
                            .document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Log.i(TAG, "User data saved to Firestore: $userData") // Đã có
                                syncUserWithBackend(userData, onSuccess, onError)
                                getUserData()
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error saving user data: ${e.message}") // Đã có
                                onError("Failed to save user data")
                            }
                    } else {
                        Log.e(TAG, "Current user is null after sign-up") // Log nếu user là null
                        onError("User creation failed")
                    }
                } else {
                    Log.e(TAG, "Sign up failed: ${task.exception?.message}") // Đã có
                    onError(task.exception?.message ?: "Sign up failed")
                }
            }
    }

    private fun syncUserWithBackend(
        userData: UserData,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.i(TAG, "Starting syncUserWithBackend with userData: $userData")
        val gson = Gson()
        val jsonBody = gson.toJson(userData)
        Log.i(TAG, "JSON body being sent: $jsonBody") // Log JSON thực tế
        apiService.syncUser(userData).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.i(TAG, "API Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    Log.i(TAG, "User synced with backend: ${response.body()?.string()}")
                    store.dispatch(Action.setUser(userData))
                    onSuccess()
                } else {
                    Log.e(TAG, "Backend sync failed: ${response.code()}")
                    Log.e(TAG, "Backend error message: ${response.message()}")
                    Log.e(TAG, "Backend error body: ${response.errorBody()?.string()}")
                    onError("Failed to sync with backend: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Backend sync error: ${t.message}")
                Log.e(TAG, "Backend sync throwable: ${t.stackTraceToString()}")
                onError("Failed to sync with backend: ${t.message}")
            }
        })
    }

    // Đăng nhập
    fun login(
        email: String,
        password: String,
        onSuccess: (UserData) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.i(TAG, "Starting login with email: $email")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Firebase login successful")
                    val user = auth.currentUser
                    if (user != null) {
                        Log.i(TAG, "Current user UID: ${user.uid}")
                        // Lấy ID Token từ Firebase
                        user.getIdToken(false).addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val idToken = tokenTask.result?.token
                                Log.i(TAG, "ID Token retrieved: $idToken")
                                if (idToken != null) {
                                    // Gửi ID Token đến backend để xác minh
                                    authenticateWithBackend(idToken, onSuccess, onError)
                                    getUserData()
                                } else {
                                    Log.e(TAG, "ID Token is null")
                                    onError("Failed to retrieve ID Token")
                                }
                            } else {
                                Log.e(TAG, "Failed to retrieve ID Token: ${tokenTask.exception?.message}")
                                onError("Failed to retrieve ID Token: ${tokenTask.exception?.message}")
                            }
                        }
                    } else {
                        Log.e(TAG, "Current user is null after login")
                        onError("Login failed: User not found")
                    }
                } else {
                    Log.e(TAG, "Login failed: ${task.exception?.message}")
                    onError(task.exception?.message ?: "Login failed")
                }
            }
    }
    fun authenticateWithBackend(
        idToken: String,
        onSuccess: (UserData) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.i(TAG, "Authenticating with backend using ID Token")
        apiService.authenticate("Bearer $idToken").enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                Log.i(TAG, "Backend auth response code: ${response.code()}")
                if (response.isSuccessful) {
                    val userData = response.body()
                    if (userData != null) {
                        Log.i(TAG, "User data from backend: $userData")
                        syncUserWithBackend(
                            userData,
                            onSuccess = {
                                onSuccess(userData)
                            },
                            onError = onError
                        )
                        Log.i(TAG, "ID Token retrieved: $idToken")

                    } else {
                        Log.e(TAG, "User data from backend is null")
                        onError("Failed to retrieve user data from backend")
                    }
                } else {
                    Log.e(TAG, "Backend auth failed: ${response.code()}")
                    Log.e(TAG, "Backend auth error: ${response.errorBody()?.string()}")
                    onError("Backend authentication failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e(TAG, "Backend auth error: ${t.message}")
                Log.e(TAG, "Backend auth throwable: ${t.stackTraceToString()}")
                onError("Failed to authenticate with backend: ${t.message}")
            }
        })
    }
}