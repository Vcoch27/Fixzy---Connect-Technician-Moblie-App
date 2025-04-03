package com.example.fixzy_ketnoikythuatvien.data.model

sealed class AuthResult {
    object Success : AuthResult()
    data class Failure(val message: String) : AuthResult()
}