package team.pacify.bookeet.data.models.finance

import team.pacify.bookeet.data.models.Entry
import java.util.*

data class Invoice(
    override var id: String = "",
    override var userId: String = "",
    val name: String = "",
    val soldProductId: String = "",
    val qty: Int = 1,
    val customerId: String = "",
    val paymentMethod: String = "",
    val date: Date = Calendar.getInstance().time,
): Entry
