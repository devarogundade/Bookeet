package team.pacify.bookeet.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.domain.repository.finance.AccountRepository
import team.pacify.bookeet.domain.repository.person.UserRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel
@Inject constructor(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> = _user

    fun updateUser(user: User) {
        viewModelScope.launch {
            _user.postValue(Resource.Loading())
            _user.postValue(userRepository.addUser(user))
        }
    }

    private val _account = MutableLiveData<Resource<Account?>>()
    val account: LiveData<Resource<Account?>> = _account


    fun getAccount(userId: String) {
        viewModelScope.launch {
            _account.postValue(Resource.Loading())
            accountRepository.getAccount(userId).collect { resource ->
                _account.postValue(resource)
            }
        }
    }

}