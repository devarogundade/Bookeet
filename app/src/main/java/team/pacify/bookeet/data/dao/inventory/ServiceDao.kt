package team.pacify.bookeet.data.dao.inventory

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.inventory.Service
import team.pacify.bookeet.utils.Resource

interface ServiceDao {
    suspend fun addService(service: Service): Service

    suspend fun deleteService(service: Service)

    suspend fun updateService(service: Service): Service

    suspend fun getService(serviceId: String): Service

    suspend fun getAllServices(
        userId: String,
        startAt: Int,
        limit: Long
    ): Flow<Resource<List<Service>>>
}