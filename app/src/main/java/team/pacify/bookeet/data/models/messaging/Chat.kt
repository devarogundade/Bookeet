package team.pacify.bookeet.data.models.messaging

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Chat(
    var id: String = "",
    val userId: String = "",
    val text: String = "",
    @ServerTimestamp
    val timeStamp: Date = Calendar.getInstance().time
)