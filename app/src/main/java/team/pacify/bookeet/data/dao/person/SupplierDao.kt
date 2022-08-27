package team.pacify.bookeet.data.dao.person

import team.pacify.bookeet.data.models.person.Supplier

interface SupplierDao {
    suspend fun getSupplier(supplierId: String): Supplier

    suspend fun getAllSuppliersByState(state: String): List<Supplier>

    suspend fun getAllSuppliersByLga(lga: String): List<Supplier>
}