package team.pacify.bookeet.data.responses

data class FsiResponse<T>(
    val status: String,
    val message: String,
    val data: T?
)