package team.pacify.bookeet.data.dao.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.utils.Resource

interface SaleDao {
    suspend fun addSale(sale: Sale): Sale

    suspend fun deleteSale(sale: Sale)

    suspend fun updateSale(sale: Sale): Sale

    suspend fun getSale(saleId: String): Sale

    suspend fun getAllSales(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Sale>>>
}