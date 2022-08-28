package team.pacify.bookeet.domain.repository.person

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.dao.person.FirebaseUserDao
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: FirebaseUserDao
) {

    suspend fun addUser(user: User): Resource<User> {
        return try {
            return Resource.Success(dao.addUser(user))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun deleteUser(user: User) {
        dao.deleteUser(user)
    }

    suspend fun updateUser(user: User): Resource<User> {
        return try {
            return Resource.Success(dao.updateUser(user))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getUser(userId: String): Flow<Resource<User?>> {
        return dao.getUser(userId)
    }

}