package team.pacify.bookeet.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.models.Product
import team.pacify.bookeet.repository.FirebaseStoreRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel
@Inject constructor(
    private val firebaseStoreRepository: FirebaseStoreRepository
) : ViewModel() {

    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> = _products

    fun getProducts(userId: String) {
        viewModelScope.launch {
            _products.postValue(Resource.Loading())
            /// to stuff
            _products.postValue(Resource.Success(emptyList()))
        }
    }


}