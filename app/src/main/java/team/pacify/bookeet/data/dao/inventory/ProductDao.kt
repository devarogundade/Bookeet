package team.pacify.bookeet.data.dao.inventory

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.utils.Resource

interface ProductDao {
    suspend fun addProduct(product: Product): Product
    suspend fun deleteProduct(product: Product)
    suspend fun updateProduct(product: Product): Product
    suspend fun getProduct(productId: String): Product
    suspend fun getAllProductForUser(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Product>>>
}