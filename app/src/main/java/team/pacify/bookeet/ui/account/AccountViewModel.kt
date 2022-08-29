package team.pacify.bookeet.ui.account

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
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _account = MutableLiveData<Resource<Account?>>()
    val account: LiveData<Resource<Account?>> = _account

    private val _user = MutableLiveData<Resource<User?>>()
    val user: LiveData<Resource<User?>> = _user

    val creating = MutableLiveData(false)

    fun getUser(userId: String) {
        viewModelScope.launch {
            _account.postValue(Resource.Loading())
            userRepository.getUser(userId).collect { resource ->
                _user.postValue(resource)
            }
        }
    }

    fun getAccount(userId: String) {
        viewModelScope.launch {
            _account.postValue(Resource.Loading())
            accountRepository.getAccount(userId).collect { resource ->
                _account.postValue(resource)
            }
        }
    }

    fun createAccount(userId: String) {
        viewModelScope.launch {
            creating.postValue(true)
            accountRepository.createAccount(userId)
            creating.postValue(false)
        }
    }


}