package team.pacify.bookeet.data.models.finance

import com.google.firebase.firestore.ServerTimestamp
import team.pacify.bookeet.data.models.Entry
import java.util.*

data class Sale(
    @ServerTimestamp
    var timeStamp: Date? = null,
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = -1,
    val paid: Double? = null,
    val soldOnCredit: Boolean = false,
    val customerId: Int? = null,
    val customerName: String = "",
    val soldOn: Date = Calendar.getInstance().time,
    override var id: String = "",
    override val userId: String = ""
) : Entry