package team.pacify.bookeet.data.models.person

import com.google.firebase.firestore.ServerTimestamp
import team.pacify.bookeet.data.models.Entry
import java.util.*

data class Customer(
    @ServerTimestamp
    var timeStamp: Date? = null,
    var email: String = "",
    var number: Int = -1,
    var name: String = "",
    override var id: String = "",
    override var userId: String = "",
    var customerPoints: Int = 1,
    var boughtProductsIDs: List<Int> = emptyList(),
    var paidServicesIDs: List<Int> = emptyList(),
    var creditProductsIDs: List<Int> = emptyList(),
    var creditServicesIDs: List<Int> = emptyList(),
) : Entry