package team.pacify.bookeet.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.pacify.bookeet.models.User
import team.pacify.bookeet.repository.FirebaseStoreRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val firebaseStoreRepository: FirebaseStoreRepository
) : ViewModel() {

    private val _user = MutableLiveData<Resource<User?>>()
    val user: LiveData<Resource<User?>> = _user

    fun getUser(userId: String?) {
        viewModelScope.launch {
            _user.postValue(Resource.Loading())
            delay(1000)
            _user.postValue(Resource.Success(null))
            delay(1000)
            _user.postValue(Resource.Loading())
            delay(1000)
            _user.postValue(
                Resource.Success(
                    User()
                )
            )
        }
    }

}