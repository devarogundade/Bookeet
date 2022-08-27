package team.pacify.bookeet.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.utils.Resource

class TransactionViewModel : ViewModel() {

    private val _transactions = MutableLiveData<Resource<List<List<Sale>>>>()
    val transactions: LiveData<Resource<List<List<Sale>>>> = _transactions

    fun getTransactions() {
        viewModelScope.launch {
            _transactions.postValue(Resource.Loading())
            _transactions.postValue(
                Resource.Success(
                    emptyList()
                )
            )
        }
    }

}