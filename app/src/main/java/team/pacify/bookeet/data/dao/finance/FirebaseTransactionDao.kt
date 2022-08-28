package team.pacify.bookeet.data.dao.finance

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseTransactionDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) : TransactionDao {
    private val rootPath: String = "transaction"

    override suspend fun addTransaction(transaction: Transaction): Transaction {
        val ref = fStore.collection(rootPath).document()
        transaction.id = ref.id
        ref.set(transaction).await()
        return transaction
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        fStore.collection(rootPath)
            .document(transaction.id)
            .delete().await()
    }

    override suspend fun updateTransaction(transaction: Transaction): Transaction {
        fStore.collection(rootPath)
            .document(transaction.id)
            .set(transaction)
            .await()
        return transaction
    }

    override suspend fun getTransaction(transactionId: String): Transaction {
        val doc = fStore.collection(rootPath)
            .document(transactionId).get().await()
        val transaction = doc.toObject<Transaction>()
        return transaction ?: throw Exception("No transaction found with ID")
    }

    override suspend fun getAllTransactions(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Transaction>>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.TRANSACTIONS_PATH)
            doc.whereEqualTo("userId", userId)
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