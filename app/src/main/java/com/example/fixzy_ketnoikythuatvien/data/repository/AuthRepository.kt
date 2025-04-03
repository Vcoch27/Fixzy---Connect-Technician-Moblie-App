package com.example.fixzy_ketnoikythuatvien.data.repository

import com.example.fixzy_ketnoikythuatvien.data.model.AuthResult

interface AuthRepository {
    suspend fun register(email: String, password: String): AuthResult
    suspend fun login(email: String, password: String): AuthResult
    fun logout()
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserEmail(): String?
}