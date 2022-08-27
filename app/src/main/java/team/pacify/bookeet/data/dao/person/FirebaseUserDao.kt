package team.pacify.bookeet.data.dao.person

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.utils.DbConstants
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

    override suspend fun getUser(userId: String): User {
        val doc = fStore.collection(DbConstants.USERS_PATH)
            .document(userId).get().await()
        val user = doc.toObject<User>()
        return user ?: throw Exception("No user found with ID")
    }

}