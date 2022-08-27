package team.pacify.bookeet.data.dao.finance

import team.pacify.bookeet.data.models.finance.Transaction

interface TransactionDao {
    suspend fun addTransaction(transaction: Transaction): Transaction

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction): Transaction

    suspend fun getTransaction(transactionId: String): Transaction

    suspend fun getAllTransactions(userId: String): List<Transaction>
}