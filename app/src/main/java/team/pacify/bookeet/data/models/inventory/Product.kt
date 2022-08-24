package team.pacify.bookeet.data.models.inventory

import com.google.firebase.firestore.FieldValue
import java.util.*

data class Product(
    var id: String = "",
    val userId: String = "",
    val name: String = "",
    val price: Double = -1.0,
    val photo: String? = null,
    val inStock: Int = 0,
    val addedOn: Date = Calendar.getInstance().time
)
