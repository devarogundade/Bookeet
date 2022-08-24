package team.pacify.bookeet.data.dao

import team.pacify.bookeet.data.models.inventory.Product

interface ProductDao {
    fun addProduct(product: Product)
    fun deleteProduct(product: Product)
    fun updateProduct(Product: Product) : Product
    fun getProduct(ProductID: String) : Product
    fun getAllProduct(userID: String) : Product
}