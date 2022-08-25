package team.pacify.bookeet.data.models.person

import team.pacify.bookeet.domain.models.Location

data class Supplier(
    val name: String,
    val details: String,
    val address : String,
    val email : String,
    val location : Location,
    val id: String,
 )