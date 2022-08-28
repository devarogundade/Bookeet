package team.pacify.bookeet.data.models.messaging

import java.util.*

data class Chat(
    var id: String = "",
    val userId: String = "",
    val text: String = "",
    val timeStamp: Date = Calendar.getInstance().time
)