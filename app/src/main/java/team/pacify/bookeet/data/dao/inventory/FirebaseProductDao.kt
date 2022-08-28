package team.pacify.bookeet.data.dao.inventory

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseProductDao @Inject constructor(
    private val fStore: FirebaseFirestore
) : ProductDao {

    override suspend fun addProduct(product: Product): Product {
        val ref = fStore.collection(DbConstants.PRODUCTS_PATH).document()
        product.id = ref.id
        ref.set(product.copy(barcodeString = ref.id)).await()
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

    override suspend fun getAllProductForUser(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Product>>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.PRODUCTS_PATH)
            doc.whereEqualTo("userId", userId)
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null)
                        launch {
                            send(
                                Resource.Error(
                                    error.localizedMessage ?: "Something went wrong"
                                )
                            )
                        }
                    if (value != null)
                        launch { send(Resource.Success(value.toObjects())) }
                }
            awaitClose {
                cancel()
            }
        }
    }
}