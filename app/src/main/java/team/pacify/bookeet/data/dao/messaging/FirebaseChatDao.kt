package team.pacify.bookeet.data.dao.messaging

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.messaging.Chat
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseChatDao @Inject constructor(
    private val fStore: FirebaseFirestore
) : ChatDao {

    override suspend fun addChat(chat: Chat): Chat {
        val ref = fStore.collection(DbConstants.CHATS_PATH).document()
        chat.id = ref.id
        ref.set(chat).await()
        return chat
    }

    override suspend fun deleteChat(chat: Chat) {
        fStore.collection(DbConstants.CHATS_PATH)
            .document(chat.id)
            .delete().await()
    }

    override suspend fun getAllChats(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Chat>>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.CHATS_PATH)
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