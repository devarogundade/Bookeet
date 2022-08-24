package team.pacify.bookeet.data.models.finance

import java.util.Date

data class Transaction(
    val title: String,
    val priceBefore: Int,
    val priceAfter: Int,
    val date: Date
)