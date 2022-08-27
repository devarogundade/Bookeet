package team.pacify.bookeet.data.models.finance

import com.google.firebase.firestore.ServerTimestamp
import team.pacify.bookeet.data.models.Entry
import java.util.Date

data class Transaction(
    @ServerTimestamp
    var timeStamp: Date?,
    val title: String,
    val priceBefore: Int,
    val priceAfter: Int,
    val date: Date,
    override var id: String,
    override var userId: String,
) : Entry