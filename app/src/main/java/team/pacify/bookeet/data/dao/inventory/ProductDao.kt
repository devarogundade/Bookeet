package team.pacify.bookeet.data.dao.inventory

import team.pacify.bookeet.data.models.inventory.Product

interface ProductDao {
    suspend fun addProduct(product: Product): Product
    suspend fun deleteProduct(product: Product)
    suspend fun updateProduct(product: Product) : Product
    suspend fun getProduct(productId: String) : Product
    suspend fun getAllProductForUser(userId: String, startAt: Int, limit: Long) : List<Product>
}