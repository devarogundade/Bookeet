package team.pacify.bookeet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.domain.repository.finance.SalesRepository
import team.pacify.bookeet.domain.repository.person.UserRepository
import team.pacify.bookeet.utils.Resource
import team.pacify.bookeet.utils.UIConstants
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val salesRepository: SalesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _sales = MutableLiveData<Resource<List<Sale>>>()
    val sales: LiveData<Resource<List<Sale>>> = _sales

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun getUser(userId: String) {
        viewModelScope.launch {
            userRepository.getUser(userId).collect { resource ->
                _user.postValue(resource.data)
            }
        }
    }

    fun getSales(userId: String) {
        viewModelScope.launch {
            val loading = sales.value is Resource.Loading
            if (loading) return@launch

            _sales.postValue(Resource.Loading())

            val resultSize = sales.value?.data?.size ?: 0
            salesRepository.getAllSales(
                userId = userId,
                startAt = resultSize + 1,
                limit = resultSize + UIConstants.FIREBASE_LOAD_SIZE
            ).collect { resource ->
                _sales.postValue(resource)
            }
        }
    }

}