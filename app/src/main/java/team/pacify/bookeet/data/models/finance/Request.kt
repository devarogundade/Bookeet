package team.pacify.bookeet.data.models.finance

import team.pacify.bookeet.data.models.Entry

data class Request(
    override var id: String,
    override val userId: String,
    val amount: Double = 0.0,
    val approved: Boolean
    ): Entry
