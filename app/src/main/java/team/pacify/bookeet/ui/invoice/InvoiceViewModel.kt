package team.pacify.bookeet.ui.invoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.utils.Resource

class InvoiceViewModel : ViewModel() {

    private val _invoices = MutableLiveData<Resource<List<Sale>>>()
    val invoices: LiveData<Resource<List<Sale>>> = _invoices

    fun getInvoices() {
        viewModelScope.launch {
            _invoices.postValue(Resource.Loading())
            delay(1000)
            _invoices.postValue(
                Resource.Success(
                    listOf(
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale(),
                        Sale()
                    )
                )
            )
        }
    }

}