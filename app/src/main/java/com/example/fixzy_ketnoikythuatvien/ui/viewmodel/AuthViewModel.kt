package com.example.fixzy_ketnoikythuatvien.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fixzy_ketnoikythuatvien.data.local.UserPreferences
import com.example.fixzy_ketnoikythuatvien.data.model.AuthResult
import com.example.fixzy_ketnoikythuatvien.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(private val repository: AuthRepository, private val userPreferences: UserPreferences) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        Log.e("LOGIN_CHECK", "DONE 2")
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.login(email, password)
            when (result) {
                is AuthResult.Success -> {
                    Log.d("AUTH_VIEWMODEL", "✅ Login Success")
                    _uiState.value = AuthUiState.Success
                }
                is AuthResult.Failure -> {
                    Log.e("AUTH_VIEWMODEL", "❌ Login Error: ${result.message}")
                    _uiState.value = AuthUiState.Error(result.message)
                }
            }
        }
    }


    fun register (email: String, password: String){
        viewModelScope.launch{
            _uiState.value = AuthUiState.Loading//cập nhật trạng thái đăng xử lí
            val result = repository.register(email, password)//
            _uiState.value = when (result) {
                is AuthResult.Success -> AuthUiState.Success
                is AuthResult.Failure -> AuthUiState.Error(result.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout() // Firebase sign out
            userPreferences.clearUser() // Xóa dữ liệu user trong DataStore
        }
    }
}
