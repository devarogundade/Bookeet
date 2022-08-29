package team.pacify.bookeet.data.dao.finance

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.clients.FsiClient
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.data.responses.FsiPager
import team.pacify.bookeet.data.responses.FsiResponse
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseTransactionDao @Inject constructor(
    private val fStore: FirebaseFirestore,
    private val fsiClient: FsiClient
) : TransactionDao {
    private val rootPath: String = "transaction"

    suspend fun transfer(
        userId: String,
        bankCode: String,
        bankAccount: Int,
        amount: Double
    ): FsiResponse<Transaction>? {
        return fsiClient.transfer(
            userId = userId,
            bankCode = bankCode,
            bankAccount = bankAccount,
            amount = amount
        ).body()
    }

    override suspend fun syncTransaction(userId: String): FsiResponse<FsiPager<Transaction>>? {
        val request = fsiClient.transactions(userId)
        val response = request.body()
        response?.data?.transactions?.forEach { transaction ->
            addOrRevokeTransaction(
                transaction.copy(
                    userId = userId
                )
            )
        }
        return response
    }

    private suspend fun addOrRevokeTransaction(transaction: Transaction) {
        try {
            getTransaction(transaction.id)
        } catch (e: Exception) {
            addTransaction(transaction)
        }
    }

    override suspend fun addTransaction(transaction: Transaction): Transaction {
        val ref = fStore.collection(DbConstants.TRANSACTIONS_PATH).document()
        transaction.id = ref.id
        ref.set(transaction).await()
        return transaction
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        fStore.collection(DbConstants.TRANSACTIONS_PATH)
            .document(transaction.id)
            .delete().await()
    }

    override suspend fun updateTransaction(transaction: Transaction): Transaction {
        fStore.collection(DbConstants.TRANSACTIONS_PATH)
            .document(transaction.id)
            .set(transaction)
            .await()
        return transaction
    }

    override suspend fun getTransaction(transactionId: String): Transaction {
        val doc = fStore.collection(DbConstants.TRANSACTIONS_PATH)
            .document(transactionId).get().await()
        return doc.toObject() ?: throw Exception("No invoice found with ID")
    }

    override suspend fun getAllTransactions(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Transaction>>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.TRANSACTIONS_PATH)
            doc.whereEqualTo("userId", userId)
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null)
                        launch {
                            send(
                                Resource.Error(
                                    error.localizedMessage ?: "Something went wrong"
                                )
                            )
                        }
                    if (value != null)
                        launch { send(Resource.Success(value.toObjects())) }
                }
            awaitClose {
                cancel()
            }
        }
    }

}