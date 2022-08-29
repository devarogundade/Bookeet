package team.pacify.bookeet.data.responses

data class FsiPager<T>(
    val page_info: PageInfo,
    val transactions: List<T>
)