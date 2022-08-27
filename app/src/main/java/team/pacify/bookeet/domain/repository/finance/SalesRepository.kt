package team.pacify.bookeet.domain.repository.finance

import team.pacify.bookeet.data.dao.finance.SaleDao
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject


class SaleRepository @Inject constructor(
    private val dao: SaleDao
) {
    suspend fun addSale(sale: Sale): Resource<Sale> {
        return try {
            return Resource.Success(dao.addSale(sale))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun deleteSale(sale: Sale) {
        dao.deleteSale(sale)
    }

    suspend fun updateSale(sale: Sale): Resource<Sale> {
        return try {
            return Resource.Success(dao.updateSale(sale))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getSale(saleId: String): Resource<Sale?> {
        return try {
            val sale = dao.getSale(saleId)
            Resource.Success(sale)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getAllSales(userId: String, startAt: Int, limit: Long): Resource<List<Sale>> {
        return try {
            val sales = dao.getAllSales(userId, startAt, limit)
            Resource.Success(sales)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

}