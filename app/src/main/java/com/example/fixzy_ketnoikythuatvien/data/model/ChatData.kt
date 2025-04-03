package com.example.fixzy_ketnoikythuatvien.data.model

import com.example.fixzy_ketnoikythuatvien.R

data class ChatData(
    val userName: String,
    val userAvatar: Int,
    val messages: List<MessageData>,
    val isNew: Boolean = false // Mặc định không có tin nhắn mới
)


data class MessageData(
    val text: String,
    val timestamp: String,
    val isSentByMe: Boolean
)
val dummyChats = listOf(
    ChatData(
        userName = "Kamal Adel",
        userAvatar = R.drawable.flavor1,
        messages = listOf(
            MessageData("Hello!", "09:00", false),
            MessageData("How are you?", "09:05", true),
            MessageData("I'm doing great!", "09:10", false),
            MessageData("That's awesome!", "09:15", true)
        ),
        isNew = true // Đánh dấu có tin nhắn mới
    ),
    ChatData(
        userName = "John Doe",
        userAvatar = R.drawable.coc,
        messages = listOf(
            MessageData("Hey, what's up?", "10:00", true),
            MessageData("Not much, you?", "10:05", false),
            MessageData("Just working on a project", "10:10", true),
            MessageData("Cool! See you later!", "10:15", false)
        ),
        isNew = false // Không có tin nhắn mới
    )
)
