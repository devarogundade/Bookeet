package team.pacify.bookeet.data.dao.inventory

import team.pacify.bookeet.data.models.inventory.Service

interface ServiceDao {
    suspend fun addService(service: Service): Service

    suspend fun deleteService(service: Service)

    suspend fun updateService(service: Service): Service

    suspend fun getService(serviceId: String): Service

    suspend fun getAllServices(userId: String, startAt: Int, limit: Long): List<Service>
}