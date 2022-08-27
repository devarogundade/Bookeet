package team.pacify.bookeet.data.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.dao.inventory.ProductDao
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.utils.DbConstants
import javax.inject.Inject

class FirebaseProductDao @Inject constructor(
    private val fStore : FirebaseFirestore
): ProductDao {

    override suspend fun addProduct(product: Product): Product {
        val ref = fStore.collection(DbConstants.PRODUCTS_PATH).document()
        product.id = ref.id
        ref.set(product).await()
        return product
      }

    override suspend fun deleteProduct(product: Product) {
        fStore.collection(DbConstants.PRODUCTS_PATH)
            .document(product.id)
            .delete().await()
    }

    override suspend fun updateProduct(product: Product): Product {
        fStore.collection(DbConstants.PRODUCTS_PATH)
            .document(product.id)
            .set(product)
            .await()
        return product
    }

    override suspend fun getProduct(productId: String): Product {
        val doc = fStore.collection(DbConstants.PRODUCTS_PATH)
            .document(productId).get().await()
        val product = doc.toObject<Product>()
        return product ?: throw Exception("No product found with ID")
     }

    override suspend fun getAllProductForUser(userId: String, startAt: Int, limit: Long): List<Product> {
        val doc = fStore.collection(DbConstants.PRODUCTS_PATH)
        val query = doc.whereEqualTo("userId", userId)
        val products = ArrayList<Product>(1)
        val result = query.get().await()

        for(r in result.documents){
            val product = r.toObject<Product>()
            if(product != null){
            products.add(product)
            }
        }
        return products
    }
}