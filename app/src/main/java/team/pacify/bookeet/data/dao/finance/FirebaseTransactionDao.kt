package team.pacify.bookeet.data.dao.finance

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.finance.Transaction
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

    override suspend fun getAllTransactions(userId: String, startAt: Int, limit: Long): List<Transaction> {
        val doc = fStore.collection(rootPath)
        val query = doc.whereEqualTo("userId", userId).orderBy("timeStamp").startAt(startAt).limit(limit)
        val entries = ArrayList<Transaction>(1)
        val result = query.get().await()

        for (r in result.documents) {
            val transaction = r.toObject<Transaction>()
            if (transaction != null) {
                entries.add(transaction)
            }
        }
        return entries
    }

}