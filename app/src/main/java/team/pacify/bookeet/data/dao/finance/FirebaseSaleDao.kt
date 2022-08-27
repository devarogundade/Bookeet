package team.pacify.bookeet.data.dao.finance

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.utils.DbConstants
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

    override suspend fun getAllSales(userId: String): List<Sale> {
        val doc = fStore.collection(DbConstants.SALES_PATH)
        val query = doc.whereEqualTo("userId", userId)
        val entries = ArrayList<Sale>(1)
        val result = query.get().await()

        for (r in result.documents) {
            val sale = r.toObject<Sale>()
            if (sale != null) {
                entries.add(sale)
            }
        }
        return entries
    }

}