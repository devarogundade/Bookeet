package team.pacify.bookeet.ui.additem

import android.net.Uri
import android.util.Log
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
import team.pacify.bookeet.domain.repository.storage.StorageRepository
import team.pacify.bookeet.utils.DbConstants.PRODUCTS_PATH
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel
@Inject
constructor(
    private val productRepository: ProductRepository,
    private val serviceRepository: ServiceRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _addProduct = MutableLiveData<Resource<Product>>()
    val addProduct: LiveData<Resource<Product>> = _addProduct

    private val _addService = MutableLiveData<Resource<Service>>()
    val addService: LiveData<Resource<Service>> = _addService

    fun addProduct(product: Product, uri: Uri?) {
        viewModelScope.launch {
            _addProduct.postValue(Resource.Loading())
            val resource = productRepository.addProduct(product)
            _addProduct.postValue(resource)

            if (resource.data != null && uri != null) {
                val upload = storageRepository.uploadFile(PRODUCTS_PATH, resource.data.id, uri)
                Log.d("TAG", "addProduct: $upload ${upload.message}")
                if (upload.data != null) {
                    productRepository.updateProduct(
                        product.copy(image = upload.data)
                    )
                }
            }
        }
    }

    fun addService(service: Service, uri: Uri?) {
        viewModelScope.launch {
            _addService.postValue(Resource.Loading())
            _addService.postValue(serviceRepository.addService(service))
        }
    }

}