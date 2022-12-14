package team.pacify.bookeet.data.dao.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.utils.Resource

interface InvoiceDao {
    suspend fun addInvoice(invoice: Invoice): Invoice

    suspend fun deleteInvoice(invoice: Invoice)

    suspend fun updateInvoice(invoice: Invoice): Invoice

    suspend fun getInvoice(invoiceId: String): Invoice

    suspend fun getAllInvoices(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Invoice>>>
}