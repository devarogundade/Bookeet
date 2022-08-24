package team.pacify.bookeet.data.dao

import team.pacify.bookeet.data.models.inventory.Product

interface ProductDao {
    suspend fun addProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun updateProduct(product: Product) : Product
    suspend fun getProduct(productId: String) : Product?
    suspend fun getAllProductForUser(userId: String) : List<Product>
}