package team.pacify.bookeet.models

import java.util.*

data class Product(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val price: Double = -1.0,
    val photo: String? = null,
    val inStock: Int = 0,
    val addedOn: Date = Calendar.getInstance().time
)
