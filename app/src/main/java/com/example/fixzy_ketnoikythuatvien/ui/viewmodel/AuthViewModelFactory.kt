//package com.example.fixzy_ketnoikythuatvien.ui.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.fixzy_ketnoikythuatvien.data.local.UserPreferences
//import com.example.fixzy_ketnoikythuatvien.redux.action.AuthViewModel
//import com.example.fixzy_ketnoikythuatvien.service.AuthService
//
//class AuthViewModelFactory(
//    private val context: AuthService,
//    userPreferences: UserPreferences
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
//            val repository = AuthRepositoryImpl(context)
//            val userPreferences = UserPreferences(context)
//            return AuthViewModel(repository, userPreferences) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
