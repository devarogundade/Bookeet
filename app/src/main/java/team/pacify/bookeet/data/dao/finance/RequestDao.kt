package team.pacify.bookeet.data.dao.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.finance.Request
import team.pacify.bookeet.utils.Resource

interface RequestDao {
    suspend fun addRequest(request: Request): Request

    suspend fun deleteRequest(request: Request)

    suspend fun getAllRequests(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Request>>>
}