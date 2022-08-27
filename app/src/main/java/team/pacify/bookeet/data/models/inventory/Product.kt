package team.pacify.bookeet.data.models.inventory

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Product(
    @ServerTimestamp
    var timeStamp: Date? = null,
    val image: String? = null,
    val barcodeString: String? = null,
    val inStock: Int = 0,
    val addedOn: Date = Calendar.getInstance().time,
    override var id: String = "",
    override val userId: String = "",
    override val name: String = "",
    override var costPrice: Double = 0.0,
    override var sellingPrice: Double = 0.0,
    override var qty: Double = 0.0,
    override var unit: String = ""
) : InventoryItem
