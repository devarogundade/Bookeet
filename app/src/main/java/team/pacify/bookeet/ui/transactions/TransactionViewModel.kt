package team.pacify.bookeet.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.domain.repository.finance.TransactionRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableLiveData<Resource<List<Transaction>>>()
    val transactions: LiveData<Resource<List<Transaction>>> = _transactions

    fun getTransactions(userId: String) {
        viewModelScope.launch {
            _transactions.postValue(Resource.Loading())
            transactionRepository.getAllTransaction(userId, 0, 10).collect { resource ->
                _transactions.postValue(resource)
            }
        }
    }

}