package team.pacify.bookeet.data.dao.person

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.person.Supplier
import team.pacify.bookeet.utils.DbConstants
import javax.inject.Inject

class FirebaseSupplierDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) : SupplierDao {

    override suspend fun getSupplier(supplierId: String): Supplier {
        val doc = fStore.collection(DbConstants.SUPPLIERS_PATH)
            .document(supplierId).get().await()
        val supplier = doc.toObject<Supplier>()
        return supplier ?: throw Exception("No supplier found with ID")
    }

    override suspend fun getAllSuppliersByState(state: String): List<Supplier> {
        val doc = fStore.collection(DbConstants.SUPPLIERS_PATH)
        val query = doc.whereEqualTo("location.state", state)
        val result = query.get().await()

        return fetchResultsFromQuery(result)
    }

    override suspend fun getAllSuppliersByLga(lga: String): List<Supplier> {
        val doc = fStore.collection(DbConstants.SUPPLIERS_PATH)
        val query = doc.whereEqualTo("location.lga", lga)
        val result = query.get().await()

        return fetchResultsFromQuery(result)
    }

    private fun fetchResultsFromQuery(
        result: QuerySnapshot,
    ): ArrayList<Supplier> {
        val entries = ArrayList<Supplier>(1)

        for (r in result.documents) {
            val supplier = r.toObject<Supplier>()
            if (supplier != null) {
                entries.add(supplier)
            }
        }
        return entries
    }

}