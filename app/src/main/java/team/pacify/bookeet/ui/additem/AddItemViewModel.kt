package team.pacify.bookeet.ui.additem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.data.models.inventory.Service
import team.pacify.bookeet.domain.repository.inventory.ProductRepository
import team.pacify.bookeet.domain.repository.inventory.ServiceRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel
@Inject
constructor(
    private val productRepository: ProductRepository,
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _addProduct = MutableLiveData<Resource<Product>>()
    val addProduct: LiveData<Resource<Product>> = _addProduct

    private val _addService = MutableLiveData<Resource<Service>>()
    val addService: LiveData<Resource<Service>> = _addService

    fun addProduct(product: Product) {
        viewModelScope.launch {
            _addProduct.postValue(Resource.Loading())
            _addProduct.postValue(productRepository.addProduct(product))
        }
    }

    fun addService(service: Service) {
        viewModelScope.launch {
            _addService.postValue(Resource.Loading())
            _addService.postValue(serviceRepository.addService(service))
        }
    }

}