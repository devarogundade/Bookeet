package team.pacify.bookeet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.utils.Resource

class HomeViewModel : ViewModel() {

    private val _sales = MutableLiveData<Resource<List<Sale>>>()
    val sales: LiveData<Resource<List<Sale>>> = _sales

    fun getSales() {
        viewModelScope.launch {
            _sales.postValue(Resource.Loading())
            _sales.postValue(
                Resource.Success(
                    listOf(
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                    )
                )
            )
        }
    }

}