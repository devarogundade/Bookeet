package team.pacify.bookeet.data.dao.finance

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
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseSaleDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) : SaleDao {

    override suspend fun addSale(sale: Sale): Sale {
        val ref = fStore.collection(DbConstants.SALES_PATH).document()
        sale.id = ref.id
        ref.set(sale).await()
        return sale
    }

    override suspend fun deleteSale(sale: Sale) {
        fStore.collection(DbConstants.SALES_PATH)
            .document(sale.id)
            .delete().await()
    }

    override suspend fun updateSale(sale: Sale): Sale {
        fStore.collection(DbConstants.SALES_PATH)
            .document(sale.id)
            .set(sale)
            .await()
        return sale
    }

    override suspend fun getSale(saleId: String): Sale {
        val doc = fStore.collection(DbConstants.SALES_PATH)
            .document(saleId).get().await()
        val sale = doc.toObject<Sale>()
        return sale ?: throw Exception("No sale found with ID")
    }

    override suspend fun getAllSales(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Sale>>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.SALES_PATH)
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