package team.pacify.bookeet.ui.qrcode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.data.models.person.Customer
import team.pacify.bookeet.domain.repository.finance.SalesRepository
import team.pacify.bookeet.domain.repository.inventory.ProductRepository
import team.pacify.bookeet.utils.Resource
import java.util.*
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel
@Inject
constructor(
    private val productRepository: ProductRepository,
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _product = MutableLiveData<Resource<Product>>()
    val product: LiveData<Resource<Product>> = _product

    fun findProduct(barcodeString: String) {
        viewModelScope.launch {
            _product.postValue(Resource.Loading())
            _product.postValue(productRepository.getProduct(barcodeString))
        }
    }

    fun recordSale(qty: Int, product: Product, customer: Customer?) {
        viewModelScope.launch {
            productRepository.updateProduct(
                product.copy(
                    inStock = product.inStock - qty
                )
            )
            salesRepository.addSale(
                Sale(
                    userId = product.userId,
                    productId = product.id,
                    timeStamp = Calendar.getInstance().time,
                    quantity = qty,
                    customerId = null
                )
            )
        }
    }

}