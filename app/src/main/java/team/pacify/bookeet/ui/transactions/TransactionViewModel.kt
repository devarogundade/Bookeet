package team.pacify.bookeet.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.models.Sale
import team.pacify.bookeet.utils.Extensions.groupByDay
import team.pacify.bookeet.utils.Resource
import java.util.*

class TransactionViewModel : ViewModel() {

    private val _transactions = MutableLiveData<Resource<List<List<Sale>>>>()
    val transactions: LiveData<Resource<List<List<Sale>>>> = _transactions

    fun getTransactions() {
        viewModelScope.launch {
            _transactions.postValue(Resource.Loading())
            _transactions.postValue(
                Resource.Success(
                    listOf(
                        Sale(
                            soldOn = Calendar.getInstance().apply {
                                time.time - 86400000
                            }.time
                        ), Sale(
                            soldOn = Calendar.getInstance().apply {
                                time.time -  86400000
                            }.time
                        ), Sale(
                            soldOn = Calendar.getInstance().apply {
                                time.time - (2 * 86400000)
                            }.time
                        ), Sale(
                            soldOn = Calendar.getInstance().apply {
                                time.time - (1.5 * 86400000)
                            }.time
                        ), Sale(
                            soldOn = Calendar.getInstance().apply {
                                time.time - (3 * 86400000)
                            }.time
                        )
                    ).groupByDay()
                )
            )
        }
    }

}