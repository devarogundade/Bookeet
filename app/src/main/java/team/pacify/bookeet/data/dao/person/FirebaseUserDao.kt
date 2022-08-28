package team.pacify.bookeet.data.dao.person

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseUserDao @Inject constructor(
    private val fStore: FirebaseFirestore
) : UserDao {
    override suspend fun addUser(user: User): User {
        fStore.collection(DbConstants.USERS_PATH).document(user.id)
            .set(user).await()
        return user
    }

    override suspend fun deleteUser(user: User) {
        fStore.collection(DbConstants.USERS_PATH)
            .document(user.id)
            .delete().await()
    }

    override suspend fun updateUser(user: User): User {
        fStore.collection(DbConstants.USERS_PATH)
            .document(user.id)
            .set(user)
            .await()
        return user
    }

    override suspend fun getUser(userId: String): Flow<Resource<User?>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.USERS_PATH)
            doc.whereEqualTo("userId", userId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        launch {
                            send(
                                Resource.Error(
                                    error.localizedMessage ?: "Something went wrong"
                                )
                            )
                        }
                    }
                    if (value != null && !value.isEmpty) {
                        launch { send(Resource.Success(value.toObjects<User>().first())) }
                    } else {
                        launch { send(Resource.Success(null)) }
                    }
                }
            awaitClose {
                cancel()
            }
        }
    }
}
