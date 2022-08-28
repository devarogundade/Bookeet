package team.pacify.bookeet.ui.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.domain.repository.finance.SalesRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _sales = MutableLiveData<Resource<List<Sale>>>()
    val sales: LiveData<Resource<List<Sale>>> = _sales

    fun getSales(userId: String) {
        viewModelScope.launch {
            _sales.postValue(Resource.Loading())
            salesRepository.getAllSales(userId, 0, 10).collect { resource ->
                _sales.postValue(resource)
            }
        }
    }

}