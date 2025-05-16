package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.AuthService
import com.example.fixzy_ketnoikythuatvien.ui.components.profileComponents.ProfileHeader
import com.example.fixzy_ketnoikythuatvien.ui.components.profileComponents.ProfileOptionList
import com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents.TopBar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit,
) {
    val state by Store.stateFlow.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val authService = AuthService(
        context = context,
        activity = null,
        onSuccess = null,
        onError = null
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 10.dp, end = 10.dp)
    ) {
        TopBar(
            navController, "Profiles",
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileHeader(state = state)
                Spacer(modifier = Modifier.height(16.dp))
                ProfileOptionList(
                    onLogout = {
//
                        authService.signOut {
                            auth.signOut()
                            onLogout()
                            Toast.makeText(context,"Logged out successfully", Toast.LENGTH_SHORT).show()

                        }
                    },
                    onEditProfile ={
                        navController.navigate("edit_profile_screen")
                    },
                    onProviderMode = {
                        navController.navigate("provider_mode_page")
                    },

                    state = state
                )
            }

        }
    }
}