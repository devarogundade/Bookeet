package team.pacify.bookeet.data.models.finance

import team.pacify.bookeet.data.models.Entry
import team.pacify.bookeet.data.models.person.Customer
import java.util.*

data class Sale(
    val productId: String = "",
    val quantity: Int = -1,
    val paid: Double? = null,
    val soldOnCredit: Boolean = false,
    val customerId: Int,
    val soldOn: Date = Calendar.getInstance().time,
    override var id: String = "",
    override val userId: String = ""
): Entry