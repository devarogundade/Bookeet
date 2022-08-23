package team.pacify.bookeet.models

data class User(
    val id: String = "",
    val name: String = "",
    val accountNumber: Long = -1,
    val emailAddress: String = "",
    val phoneNumber: String = "",
    val bvn: String = "",
    val photo: String? = null
)
