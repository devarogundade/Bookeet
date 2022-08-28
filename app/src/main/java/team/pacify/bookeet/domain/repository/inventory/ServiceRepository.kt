package team.pacify.bookeet.domain.repository.inventory

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.dao.inventory.ServiceDao
import team.pacify.bookeet.data.models.inventory.Service
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class ServiceRepository @Inject constructor(
    private val dao: ServiceDao
) {
    suspend fun addService(service: Service): Resource<Service> {
        return try {
            return Resource.Success(dao.addService(service))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun deleteService(service: Service) {
        dao.deleteService(service)
    }

    suspend fun updateService(service: Service): Resource<Service> {
        return try {
            return Resource.Success(dao.updateService(service))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getService(serviceId: String): Resource<Service> {
        return try {
            val service = dao.getService(serviceId)
            Resource.Success(service)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getAllServices(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Service>>> {
        return dao.getAllServices(userId, startAt, limit)
    }

}