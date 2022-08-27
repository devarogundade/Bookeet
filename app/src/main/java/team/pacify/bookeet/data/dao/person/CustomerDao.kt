package team.pacify.bookeet.data.dao.person

import team.pacify.bookeet.data.models.person.Customer

interface CustomerDao {
    suspend fun addCustomer(customer: Customer): Customer

    suspend fun deleteCustomer(customer: Customer)

    suspend fun updateCustomer(customer: Customer): Customer

    suspend fun getCustomer(customerId: String): Customer

    suspend fun getAllCustomers(userId: String): List<Customer>
}