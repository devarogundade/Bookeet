package team.pacify.bookeet.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.domain.repository.inventory.ProductRepository
import team.pacify.bookeet.utils.Resource
import team.pacify.bookeet.utils.UIConstants.FIREBASE_LOAD_SIZE
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel
@Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> = _products

    fun getProducts(userId: String) {
        viewModelScope.launch {
            val loading = products.value is Resource.Loading
            if (loading) return@launch

            _products.postValue(Resource.Loading())

            val resultSize = products.value?.data?.size ?: 0
            _products.postValue(
                productRepository.getAllProductForUser(
                    userId = userId,
                    startAt = resultSize,
                    limit = resultSize + FIREBASE_LOAD_SIZE
                )
            )
        }
    }


}