package team.pacify.bookeet.data.models.finance

import team.pacify.bookeet.data.models.Entry

data class Account(
    override var id: String = "",
    override var userId: String = "",
    var balance: Double = 0.0,
    var accNo: String,
    var bankName: String,
    var createdAt: String,
): Entry
