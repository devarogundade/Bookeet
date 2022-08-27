package team.pacify.bookeet.data.models.person

import team.pacify.bookeet.data.models.Entry

data class User(
    override var id: String = "",
    val name: String = "",
    val accountNumber: Long = -1,
    val emailAddress: String = "",
    val phoneNumber: String = "",
    val bvn: String = "",
    val photo: String? = null,
    override val userId: String
) : Entry
