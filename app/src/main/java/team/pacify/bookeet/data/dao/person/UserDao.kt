package team.pacify.bookeet.data.dao.person

import team.pacify.bookeet.data.models.person.User

interface UserDao {
    suspend fun addUser(user: User): User
    suspend fun deleteUser(user: User)
    suspend fun updateUser(user: User) : User
    suspend fun getUser(userId: String) : User?
}