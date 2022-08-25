package team.pacify.bookeet.data.models.person

import team.pacify.bookeet.data.models.Entry

data class Customer(
    var email: String,
    var number: Int,
    override var id: String,
    override var userId: String,
    var customerPoints: Int,
    var boughtProductsIDs: List<Int>,
    var paidServicesIDs: List<Int>,
    var creditProductsIDs: List<Int>,
    var creditServicesIDs: List<Int>
 ): Entry