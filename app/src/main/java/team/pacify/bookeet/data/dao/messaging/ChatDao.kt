package team.pacify.bookeet.data.dao.messaging

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.messaging.Chat
import team.pacify.bookeet.utils.Resource

interface ChatDao {
    suspend fun addChat(chat: Chat): Chat
    suspend fun deleteChat(chat: Chat)
    suspend fun getAllChats(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Chat>>>
}