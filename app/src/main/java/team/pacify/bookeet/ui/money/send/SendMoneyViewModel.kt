package team.pacify.bookeet.ui.money.send

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Sale

class SendMoneyViewModel : ViewModel() {

    private val _banks = MutableLiveData<List<Sale>>()
    val banks: LiveData<List<Sale>> = _banks

    fun getBanks() {
        viewModelScope.launch {
            _banks.postValue(
                listOf(
                    Sale(
                        customerName = "Sterling"
                    ),
                    Sale(
                        customerName = "GTBank"
                    ),
                    Sale(
                        customerName = "KudaMFB"
                    ),
                    Sale(
                        customerName = "Polaris"
                    ),
                )
            )
        }
    }


}