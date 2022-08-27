package team.pacify.bookeet.data.dao.person

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.person.Customer
import javax.inject.Inject

class FirebaseCustomerDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) : CustomerDao {
    private val rootPath: String = "customer"

    override suspend fun addCustomer(customer: Customer): Customer {
        val ref = fStore.collection(rootPath).document()
        customer.id = ref.id
        ref.set(customer).await()
        return customer
    }

    override suspend fun deleteCustomer(customer: Customer) {
        fStore.collection(rootPath)
            .document(customer.id)
            .delete().await()
    }

    override suspend fun updateCustomer(customer: Customer): Customer {
        fStore.collection(rootPath)
            .document(customer.id)
            .set(customer)
            .await()
        return customer
    }

    override suspend fun getCustomer(customerId: String): Customer {
        val doc = fStore.collection(rootPath)
            .document(customerId).get().await()
        val customer = doc.toObject<Customer>()
        return customer ?: throw Exception("No customer found with ID")
    }

    override suspend fun getAllCustomers(userId: String): List<Customer> {
        val doc = fStore.collection(rootPath)
        val query = doc.whereEqualTo("userId", userId)
        val entries = ArrayList<Customer>(1)
        val result = query.get().await()

        for (r in result.documents) {
            val customer = r.toObject<Customer>()
            if (customer != null) {
                entries.add(customer)
            }
        }
        return entries
    }

}