package team.pacify.bookeet.data.models.finance

import team.pacify.bookeet.data.models.Entry
import java.util.Date

data class Transaction(
    val title: String,
    val priceBefore: Int,
    val priceAfter: Int,
    val date: Date,
    override var id: String,
    override var userId: String
) : Entry