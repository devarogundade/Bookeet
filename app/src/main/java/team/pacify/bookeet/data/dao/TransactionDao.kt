package team.pacify.bookeet.data.dao

import team.pacify.bookeet.data.models.finance.Transaction

interface TransactionDao {
    suspend fun addTransaction(transaction: Transaction): Transaction
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction) : Transaction
    suspend fun getTransaction(transactionID: String) : Transaction
    suspend fun getAllTransaction(userID: String) : Transaction
}