package team.pacify.bookeet.domain.repository.messaging

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.dao.messaging.ChatDao
import team.pacify.bookeet.data.models.messaging.Chat
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val dao: ChatDao
) {
    suspend fun addChat(chat: Chat): Resource<Chat> {
        return try {
            return Resource.Success(dao.addChat(chat))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun deleteChat(chat: Chat) {
        dao.deleteChat(chat)
    }

    suspend fun getAllChats(userId: String, startAt: Int, limit: Long): Flow<Resource<List<Chat>>> {
        return dao.getAllChats(userId, startAt, limit)
    }

}