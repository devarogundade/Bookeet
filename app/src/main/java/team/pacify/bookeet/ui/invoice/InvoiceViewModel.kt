package team.pacify.bookeet.ui.invoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.domain.repository.finance.InvoiceRepository
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel
@Inject constructor(
    private val invoiceRepository: InvoiceRepository
) : ViewModel() {

    private val _invoices = MutableLiveData<Resource<List<Invoice>>>()
    val invoices: LiveData<Resource<List<Invoice>>> = _invoices

    fun getInvoices(userId: String) {
        viewModelScope.launch {
            _invoices.postValue(Resource.Loading())
            invoiceRepository.getAllInvoices(userId, 0, 10).collect { resource ->
                _invoices.postValue(resource)
            }
        }
    }

}