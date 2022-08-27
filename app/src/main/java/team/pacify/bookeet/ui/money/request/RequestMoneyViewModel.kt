package team.pacify.bookeet.ui.money.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.utils.Resource

class RequestMoneyViewModel : ViewModel() {

    private val _requests = MutableLiveData<Resource<List<Sale>>>()
    val requests: LiveData<Resource<List<Sale>>> = _requests

    fun getRequests() {
        viewModelScope.launch {
            _requests.postValue(Resource.Loading())
            delay(1000)
            _requests.postValue(
                Resource.Success(
                    emptyList()
                )
            )
        }
    }

}