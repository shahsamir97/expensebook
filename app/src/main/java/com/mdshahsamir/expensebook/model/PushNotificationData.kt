package com.mdshahsamir.expensebook.model

data class PushNotificationData(
    val title: String,
    val body: String,
    val channelId: String,
)