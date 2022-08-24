package team.pacify.bookeet.data.models.finance

import java.util.*

data class Sale(
    val id: String = "",
    val productId: String = "",
    val quantity: Int = -1,
    val paid: Double? = null,
    val soldOnCredit: Boolean = false,
    val customerName: String? = null,
    val customerPhone: String? = null,
    val customerAddress: String? = null,
    val soldOn: Date = Calendar.getInstance().time
)