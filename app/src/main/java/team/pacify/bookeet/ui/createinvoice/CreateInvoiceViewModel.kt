package team.pacify.bookeet.ui.createinvoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.data.models.person.Customer
import team.pacify.bookeet.domain.repository.finance.InvoiceRepository
import team.pacify.bookeet.domain.repository.inventory.ProductRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class CreateInvoiceViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val invoiceRepository: InvoiceRepository
) : ViewModel() {

    val products = MutableLiveData<List<Product>>()
    val customers = MutableLiveData<List<Customer>>()

    private val _invoice = MutableLiveData<Resource<Invoice>>()
    val invoice: LiveData<Resource<Invoice>> = _invoice

    fun addInvoice(invoice: Invoice) {
        viewModelScope.launch {
            _invoice.postValue(Resource.Loading())
            _invoice.postValue(invoiceRepository.addInvoice(invoice))
        }
    }

    fun getProducts(userId: String) {
        viewModelScope.launch {
            productRepository.getAllProductForUser(userId, 0, 20).collect { resource ->
                resource.data?.let {
                    products.postValue(it)
                }
            }
        }
    }

    fun getCustomers(userId: String) {
        viewModelScope.launch {
            customers.postValue(
                listOf(
                    Customer(name = "Rhoda"),
                    Customer(name = "Jerry"),
                    Customer(name = "Justin"),
                    Customer(name = "Chisom"),
                    Customer(name = "Ibrahim"),
                )
            )
        }
    }

}