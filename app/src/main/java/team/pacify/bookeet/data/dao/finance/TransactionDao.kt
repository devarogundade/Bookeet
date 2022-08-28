package team.pacify.bookeet.data.dao.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.utils.Resource

interface TransactionDao {
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