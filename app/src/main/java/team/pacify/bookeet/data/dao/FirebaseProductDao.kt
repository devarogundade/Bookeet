package team.pacify.bookeet.data.dao

import com.google.firebase.firestore.FirebaseFirestore
import team.pacify.bookeet.data.models.inventory.Product
import javax.inject.Inject

class FirebaseProductDao @Inject constructor(
    private val fStore : FirebaseFirestore
): ProductDao {

    override fun addProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun updateProduct(Product: Product): Product {
        TODO("Not yet implemented")
    }

    override fun getProduct(ProductID: String): Product {
        TODO("Not yet implemented")
    }

    override fun getAllProduct(userID: String): Product {
        TODO("Not yet implemented")
    }
}