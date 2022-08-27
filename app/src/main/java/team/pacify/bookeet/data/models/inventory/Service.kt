package team.pacify.bookeet.data.models.inventory

import java.util.*

data class Service(
    val image: String = "",
    val renderedOn: Date = Calendar.getInstance().time,
    override var id: String = "",
    override val userId: String = "",
    override val name: String = "",
    override var costPrice: Double,
    override var sellingPrice: Double,
    override var qty: Double,
    override var unit: String,
): InventoryItem
