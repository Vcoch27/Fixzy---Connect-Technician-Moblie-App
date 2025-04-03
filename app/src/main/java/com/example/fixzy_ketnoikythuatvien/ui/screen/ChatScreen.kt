package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.data.model.dummyChats
import com.example.fixzy_ketnoikythuatvien.ui.components.chatComponents.ChatItem
import com.example.fixzy_ketnoikythuatvien.ui.components.chatComponents.NewChatHeader
import com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents.TopBar

@Composable
fun ChatScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Recent") }
    val chats = remember { mutableStateListOf(*dummyChats.toTypedArray()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 28.dp, start = 10.dp, end = 10.dp)) {
        // TopBar + Bộ lọc
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(end = 10.dp), // ✅ Giữ khoảng cách hai bên
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TopBar(
                navController, "Chats",
                modifier = Modifier.weight(1f) // ✅ Đảm bảo nó không chiếm toàn bộ chiều rộng
            )


        }
        NewChatHeader()

        // Danh sách thông báo
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chats) { chat ->
                ChatItem(chat) { selectedChat ->
                    navController.navigate("chat_screen/${selectedChat.userName}")
                }
            }
        }
    }
}

