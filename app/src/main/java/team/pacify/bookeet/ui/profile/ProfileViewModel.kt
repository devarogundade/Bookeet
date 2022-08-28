package team.pacify.bookeet.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.domain.repository.person.UserRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<Resource<User?>>()
    val user: LiveData<Resource<User?>> = _user

    fun getUser(userId: String) {
        viewModelScope.launch {
            _user.postValue(Resource.Loading())
            userRepository.getUser(userId).collect { resource ->
                _user.postValue(resource)
            }
        }
    }

}