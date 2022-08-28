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
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseInvoiceDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) : InvoiceDao {

    override suspend fun addInvoice(invoice: Invoice): Invoice {
        val ref = fStore.collection(DbConstants.INVOICES_PATH).document()
        invoice.id = ref.id
        ref.set(invoice).await()
        return invoice
    }

    override suspend fun deleteInvoice(invoice: Invoice) {
        fStore.collection(DbConstants.INVOICES_PATH)
            .document(invoice.id)
            .delete().await()
    }

    override suspend fun updateInvoice(invoice: Invoice): Invoice {
        fStore.collection(DbConstants.INVOICES_PATH)
            .document(invoice.id)
            .set(invoice)
            .await()
        return invoice
    }

    override suspend fun getInvoice(invoiceId: String): Invoice {
        val doc = fStore.collection(DbConstants.INVOICES_PATH)
            .document(invoiceId).get().await()
        val invoice = doc.toObject<Invoice>()
        return invoice ?: throw Exception("No invoice found with ID")
    }

    override suspend fun getAllInvoices(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Invoice>>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.INVOICES_PATH)
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