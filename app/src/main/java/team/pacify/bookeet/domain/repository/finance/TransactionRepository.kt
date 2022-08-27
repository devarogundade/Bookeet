package team.pacify.bookeet.domain.repository.finance

import team.pacify.bookeet.data.dao.finance.TransactionDao
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject


class TransactionRepository @Inject constructor(
    private val dao: TransactionDao
) {
    suspend fun getTransaction(transactionId: String): Resource<Transaction?> {
        return try {
            val transaction = dao.getTransaction(transactionId)
            Resource.Success(transaction)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }
}