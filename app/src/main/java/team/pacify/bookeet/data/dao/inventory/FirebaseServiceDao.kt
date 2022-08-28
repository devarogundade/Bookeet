package team.pacify.bookeet.data.dao.inventory

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.inventory.Service
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
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

    override suspend fun getAllServices(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Service>>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.SERVICES_PATH)
            doc.whereEqualTo("userId", userId)
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