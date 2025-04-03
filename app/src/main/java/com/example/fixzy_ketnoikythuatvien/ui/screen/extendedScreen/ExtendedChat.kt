package com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.data.model.ChatData
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun ExtendedChat(chat: ChatData, navController: NavController) {
    var messageText by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Thanh tiêu đề
        Box(modifier = Modifier.background(AppTheme.colors.mainColor)) {
            TopAppBar(
                modifier = Modifier.padding(top=25.dp),
                title = {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = chat.userAvatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = chat.userName,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = AppTheme.colors.mainColor,
                actions = {
                    IconButton(onClick = { /* TODO: Options */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                },
                )
        }


        // Danh sách tin nhắn
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            state = scrollState,
            reverseLayout = true
        ) {
            items(chat.messages) { message ->
                val isSender = message.isSentByMe
                val backgroundColor = if (isSender) AppTheme.colors.mainColor else Color(0xFFF5F5F5)
                val textColor = if (isSender) Color.White else Color.Black
                val alignment = if (isSender) Alignment.End else Alignment.Start

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = alignment
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(backgroundColor)
                            .padding(12.dp)
                    ) {
                        Text(text = message.text, color = textColor, fontSize = 16.sp)
                    }
                    Text(
                        text = message.timestamp,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }

        // Trường nhập tin nhắn
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Write a message...") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { /* TODO: Send Message */ }) {
                Icon(
                    Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = AppTheme.colors.mainColor
                )
            }
        }
    }
}
