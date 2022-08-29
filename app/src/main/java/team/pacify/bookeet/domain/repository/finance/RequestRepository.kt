package team.pacify.bookeet.domain.repository.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.dao.finance.RequestDao
import team.pacify.bookeet.data.models.finance.Request
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val dao: RequestDao
) {
    suspend fun addRequest(request: Request): Resource<Request> {
        return try {
            return Resource.Success(dao.addRequest(request))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun deleteRequest(request: Request) {
        dao.deleteRequest(request)
    }

    suspend fun getAllRequests(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Request>>> {
        return dao.getAllRequests(userId, startAt, limit)
    }

}