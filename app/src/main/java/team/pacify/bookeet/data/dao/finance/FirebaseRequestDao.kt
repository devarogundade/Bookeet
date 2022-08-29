package team.pacify.bookeet.data.dao.finance

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.finance.Request
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseRequestDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) : RequestDao {

    override suspend fun addRequest(request: Request): Request {
        val ref = fStore.collection(DbConstants.REQUESTS_PATH).document()
        request.id = ref.id
        ref.set(request).await()
        return request
    }

    override suspend fun deleteRequest(request: Request) {
        fStore.collection(DbConstants.REQUESTS_PATH)
            .document(request.id)
            .delete().await()
    }

    override suspend fun getAllRequests(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Request>>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.REQUESTS_PATH)
            doc.whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
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