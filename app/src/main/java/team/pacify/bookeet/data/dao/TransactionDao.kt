package team.pacify.bookeet.data.dao

import team.pacify.bookeet.data.models.finance.Transaction

interface TransactionDao {
    fun addTransaction()
    fun deleteTransaction()
    fun updateTransaction(transaction: Transaction) : Transaction
    fun getTransaction(transactionID: String) : Transaction
    fun getAllTransaction(userID: String) : Transaction
}