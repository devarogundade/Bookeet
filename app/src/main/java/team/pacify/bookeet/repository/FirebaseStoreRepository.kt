package team.pacify.bookeet.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.models.Product
import team.pacify.bookeet.models.Sale
import team.pacify.bookeet.models.User
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseStoreRepository
@Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    suspend fun getUser(userId: String): Resource<User?> {
        return try {
            val data = firebaseFirestore.collection(USERS_REF).document(userId)
                .get().await()
            val user = data.toObject(User::class.java)
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun setOrUpdateUser(user: User): Resource<Boolean> {
        return try {
            val data = firebaseFirestore.collection(USERS_REF).document(user.id)
                .set(user).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getProducts(userId: String): Resource<List<Product>> {
        return try {
            val data = firebaseFirestore.collection(PRODUCTS_REF)
                .whereEqualTo("userId", userId)
                .get().await()

            val products = data.toObjects(Product::class.java)
            Resource.Success(products)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getItems(productId: String): Resource<List<Sale>> {
        return try {
            val data = firebaseFirestore.collection(ITEMS_REF)
                .whereEqualTo("productId", productId)
                .get().await()

            val sales = data.toObjects(Sale::class.java)
            Resource.Success(sales)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    companion object {
        const val USERS_REF = "users"
        const val PRODUCTS_REF = "products"
        const val ITEMS_REF = "items"
    }

}