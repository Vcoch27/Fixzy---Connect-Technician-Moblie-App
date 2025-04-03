package com.example.fixzy_ketnoikythuatvien.data.repository

import android.content.Context
import android.util.Log
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.data.model.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            Log.e("LOGIN_REPO", "❌ Login failed", e)
            val message = when (e) {
                is FirebaseAuthInvalidUserException ->
                    context.getString(R.string.error_user_not_found)

                is FirebaseAuthInvalidCredentialsException ->
                    context.getString(R.string.error_invalid_credentials)

                is FirebaseAuthException ->
                    context.getString(R.string.error_auth_general)

                else ->
                    context.getString(R.string.error_unexpected)
            }
            AuthResult.Failure(message)
        }
    }

    override suspend fun register(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            val message = when (e) {
                is FirebaseAuthUserCollisionException ->
                    context.getString(R.string.error_account_exists)

                is FirebaseAuthWeakPasswordException ->
                    context.getString(R.string.error_weak_password)

                is FirebaseAuthInvalidCredentialsException ->
                    context.getString(R.string.error_invalid_email)

                is FirebaseAuthException ->
                    context.getString(R.string.error_auth_general)

                else ->
                    context.getString(R.string.error_unexpected)
            }
            AuthResult.Failure(message)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUserEmail(): String? {
        return firebaseAuth.currentUser?.email
    }
}
