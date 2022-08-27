package team.pacify.bookeet.data.models.inventory

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Product(
    @ServerTimestamp
    var timeStamp: Date?,
    val image: String? = null,
    val barcodeString: String? = null,
    val inStock: Int = 0,
    val addedOn: Date = Calendar.getInstance().time,
    override var id: String = "",
    override val userId: String = "",
    override val name: String,
    override var costPrice: Double,
    override var sellingPrice: Double,
    override var qty: Double,
    override var unit: String
): InventoryItem
