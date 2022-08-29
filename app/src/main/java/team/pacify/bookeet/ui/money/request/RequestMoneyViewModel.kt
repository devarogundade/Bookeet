package team.pacify.bookeet.ui.money.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Request
import team.pacify.bookeet.domain.repository.finance.RequestRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class RequestMoneyViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _requests = MutableLiveData<Resource<List<Request>>>()
    val requests: LiveData<Resource<List<Request>>> = _requests

    val createRequest = MutableLiveData<Resource<Request>>()

    fun getRequests(userId: String) {
        viewModelScope.launch {
            _requests.postValue(Resource.Loading())
            requestRepository.getAllRequests(userId, 0, 10).collect { resource ->
                _requests.postValue(resource)
            }
        }
    }

    fun createRequest(request: Request) {
        viewModelScope.launch {
            createRequest.postValue(Resource.Loading())
            createRequest.postValue(requestRepository.addRequest(request))
        }
    }

}