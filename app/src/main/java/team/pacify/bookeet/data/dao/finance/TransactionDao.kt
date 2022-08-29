package team.pacify.bookeet.data.dao.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.data.responses.FsiPager
import team.pacify.bookeet.data.responses.FsiResponse
import team.pacify.bookeet.utils.Resource

interface TransactionDao {
    suspend fun syncTransaction(userId: String): FsiResponse<FsiPager<Transaction>>?

    suspend fun transfer(transaction: Transaction): FsiResponse<Transaction>?

    suspend fun addTransaction(transaction: Transaction): Transaction

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction): Transaction

    suspend fun getTransaction(transactionId: String): Transaction

    suspend fun getAllTransactions(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Transaction>>>
}