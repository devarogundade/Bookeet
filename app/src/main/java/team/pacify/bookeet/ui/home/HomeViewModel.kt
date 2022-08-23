package team.pacify.bookeet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import team.pacify.bookeet.models.Sale
import team.pacify.bookeet.utils.Resource
import java.util.*

class HomeViewModel : ViewModel() {

    private val _sales = MutableLiveData<Resource<List<Sale>>>()
    val sales: LiveData<Resource<List<Sale>>> = _sales

    fun getSales() {
        viewModelScope.launch {
            _sales.postValue(Resource.Loading())
            _sales.postValue(
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
                    )
                )
            )
        }
    }

}