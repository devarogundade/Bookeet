package team.pacify.bookeet.data.clients

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.data.responses.FsiPager
import team.pacify.bookeet.data.responses.FsiResponse

interface FsiClient {

    @POST("create")
    suspend fun createVirtualAccount(
        @Query("user_id") userId: String
    ): Response<FsiResponse<Account>>

    @GET("account")
    suspend fun virtualAccount(
        @Query("user_id") userId: String
    ): Response<FsiResponse<Account>>

    @GET("transactions")
    suspend fun transactions(
        @Query("user_id") userId: String
    ): Response<FsiResponse<FsiPager<Transaction>>>

    @POST("transfer")
    suspend fun transfer(
        @Query("user_id") userId: String,
        @Query("bank_code") bankCode: String,
        @Query("bank_account") accountNumber: String,
        @Query("amount") amount: Double
    ): Response<FsiResponse<Transaction>>

}