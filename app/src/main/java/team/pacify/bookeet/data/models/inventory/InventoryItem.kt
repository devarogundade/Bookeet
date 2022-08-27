package team.pacify.bookeet.data.models.inventory

import team.pacify.bookeet.data.models.Entry

interface InventoryItem : Entry {
    val name: String
    var costPrice: Double
    var sellingPrice: Double
    var qty: Double
    var unit: String
}