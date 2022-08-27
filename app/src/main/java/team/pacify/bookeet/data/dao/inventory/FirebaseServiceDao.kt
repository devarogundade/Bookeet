package team.pacify.bookeet.data.dao.inventory

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.inventory.Service
import team.pacify.bookeet.utils.DbConstants
import javax.inject.Inject

class FirebaseServiceDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) : ServiceDao {

    override suspend fun addService(service: Service): Service {
        val ref = fStore.collection(DbConstants.SERVICES_PATH).document()
        service.id = ref.id
        ref.set(service).await()
        return service
    }

    override suspend fun deleteService(service: Service) {
        fStore.collection(DbConstants.SERVICES_PATH)
            .document(service.id)
            .delete().await()
    }

    override suspend fun updateService(service: Service): Service {
        fStore.collection(DbConstants.SERVICES_PATH)
            .document(service.id)
            .set(service)
            .await()
        return service
    }

    override suspend fun getService(serviceId: String): Service {
        val doc = fStore.collection(DbConstants.SERVICES_PATH)
            .document(serviceId).get().await()
        val service = doc.toObject<Service>()
        return service ?: throw Exception("No service found with ID")
    }

    override suspend fun getAllServices(userId: String, startAt: Int, limit: Long): List<Service> {
        val doc = fStore.collection(DbConstants.SERVICES_PATH)
        val query = doc.whereEqualTo("userId", userId).orderBy("timeStamp").startAt(startAt).limit(limit)
        val entries = ArrayList<Service>(1)
        val result = query.get().await()

        for (r in result.documents) {
            val service = r.toObject<Service>()
            if (service != null) {
                entries.add(service)
            }
        }
        return entries
    }

}