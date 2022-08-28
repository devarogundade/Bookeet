package team.pacify.bookeet.domain.repository.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.dao.finance.TransactionDao
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject


class TransactionRepository @Inject constructor(
    private val dao: TransactionDao
) {
    suspend fun addTransaction(transaction: Transaction): Resource<Transaction> {
        return try {
            return Resource.Success(dao.addTransaction(transaction))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction): Resource<Transaction> {
        return try {
            return Resource.Success(dao.updateTransaction(transaction))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getTransaction(transactionId: String): Resource<Transaction?> {
        return try {
            val transaction = dao.getTransaction(transactionId)
            Resource.Success(transaction)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getAllTransaction(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Transaction>>> {
        return dao.getAllTransactions(userId, startAt, limit)
    }

}