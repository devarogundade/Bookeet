package team.pacify.bookeet.data.models.finance

import com.google.firebase.firestore.ServerTimestamp
import team.pacify.bookeet.data.models.Entry
import java.util.*

data class Invoice(
    @ServerTimestamp
    var timeStamp: Date? = null,
    override var id: String = "",
    val invoiceId: String = "",
    override var userId: String = "",
    val name: String = "",
    val soldProductId: String = "",
    val soldProductName: String = "",
    val soldProductAmount: Double = 0.0,
    val amountReceived: Double = 0.0,
    val qty: Int = 1,
    val customerId: String = "",
    val customerName: String = "",
    val paymentMethod: String = "",
    val date: Date = Calendar.getInstance().time,
) : Entry
