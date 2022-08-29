package team.pacify.bookeet.ui.money.send

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Bank
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.domain.repository.finance.TransactionRepository
import team.pacify.bookeet.utils.Resource
import team.pacify.bookeet.utils.UIConstants.Banks
import javax.inject.Inject

@HiltViewModel
class SendMoneyViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val sending = MutableLiveData<Resource<Transaction>>()

    private val _banks = MutableLiveData<List<Bank>>()
    val banks: LiveData<List<Bank>> = _banks

    fun getBanks() {
        viewModelScope.launch {
            _banks.postValue(Banks)
        }
    }

    fun sendMoney(transaction: Transaction) {
        viewModelScope.launch {
            sending.postValue(Resource.Loading())
            sending.postValue(transactionRepository.addTransaction(transaction))
        }
    }


}