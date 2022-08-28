package team.pacify.bookeet.domain.repository.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.dao.finance.InvoiceDao
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject


class InvoiceRepository @Inject constructor(
    private val dao: InvoiceDao
) {
    suspend fun addInvoice(invoice: Invoice): Resource<Invoice> {
        return try {
            return Resource.Success(dao.addInvoice(invoice))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun deleteInvoice(invoice: Invoice) {
        dao.deleteInvoice(invoice)
    }

    suspend fun updateInvoice(invoice: Invoice): Resource<Invoice> {
        return try {
            return Resource.Success(dao.updateInvoice(invoice))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getInvoice(invoiceId: String): Resource<Invoice?> {
        return try {
            val invoice = dao.getInvoice(invoiceId)
            Resource.Success(invoice)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getAllInvoices(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Invoice>>> {
        return dao.getAllInvoices(userId, startAt, limit)
    }

}