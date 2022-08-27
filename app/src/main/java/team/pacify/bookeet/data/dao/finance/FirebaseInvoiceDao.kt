package team.pacify.bookeet.data.dao.finance

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.utils.DbConstants
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

    override suspend fun getAllInvoices(userId: String): List<Invoice> {
        val doc = fStore.collection(DbConstants.INVOICES_PATH)
        val query = doc.whereEqualTo("userId", userId)
        val entries = ArrayList<Invoice>(1)
        val result = query.get().await()

        for (r in result.documents) {
            val invoice = r.toObject<Invoice>()
            if (invoice != null) {
                entries.add(invoice)
            }
        }
        return entries
    }

}