package com.tip_n_go.data.incoming

import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.NotificationDeserializer

@JsonAdapter(NotificationDeserializer::class)
class Notification(
    val id: Int,
    val date: String?,
    val text: String?,
    val isRead: Boolean
)
