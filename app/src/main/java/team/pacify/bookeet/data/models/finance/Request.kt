package team.pacify.bookeet.data.models.finance

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Request(
    var id: String = "",
    val userId: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val amount: Double = 0.0,
    val narration: String? = null,
    @ServerTimestamp
    val timeStamp: Date = Calendar.getInstance().time
)
