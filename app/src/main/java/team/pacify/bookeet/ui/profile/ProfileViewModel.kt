package team.pacify.bookeet.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.utils.Resource

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<Resource<User?>>()
    val user: LiveData<Resource<User?>> = _user

    fun getUser(userId: String) {
        viewModelScope.launch {
            _user.postValue(Resource.Loading())
            delay(1000)
            _user.postValue(Resource.Success(null))
        }
    }

}