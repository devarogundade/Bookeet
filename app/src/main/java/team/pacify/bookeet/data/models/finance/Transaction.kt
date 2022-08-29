package team.pacify.bookeet.data.models.finance

import com.google.firebase.firestore.ServerTimestamp
import team.pacify.bookeet.data.models.Entry
import java.util.*

data class Transaction(
    @ServerTimestamp
    var timeStamp: Date? = null,
    val narration: String = "",
    val priceBefore: Double = 0.0,
    val priceAfter: Double = 0.0,
    val date: Date? = null,
    val type: String = "debit",
    override var id: String = "",
    override var userId: String = "",
) : Entry