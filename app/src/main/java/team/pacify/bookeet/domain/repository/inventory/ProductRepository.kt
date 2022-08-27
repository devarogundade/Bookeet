package team.pacify.bookeet.domain.repository.finance

import team.pacify.bookeet.data.dao.inventory.ProductDao
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject


class ProductRepository @Inject constructor(
    private val dao: ProductDao
) {
    suspend fun addProduct(product: Product): Resource<Product> {
        return try {
            return Resource.Success(dao.addProduct(product))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun deleteProduct(product: Product) {
        dao.deleteProduct(product)
    }

    suspend fun updateProduct(product: Product): Resource<Product> {
        return try {
            return Resource.Success(dao.updateProduct(product))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getProduct(productId: String): Resource<Product> {
        return try {
            val product = dao.getProduct(productId)
            Resource.Success(product)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getAllProductForUser(userId: String, startAt: Int, limit: Long): Resource<List<Product>> {
        return try {
            val products = dao.getAllProductForUser(userId, startAt, limit)
            Resource.Success(products)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

}