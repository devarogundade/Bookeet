package team.pacify.bookeet.data.dao.person

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.utils.Resource

interface UserDao {
    suspend fun addUser(user: User): User
    suspend fun deleteUser(user: User)
    suspend fun updateUser(user: User): User
    suspend fun getUser(userId: String): Flow<Resource<User?>>
}