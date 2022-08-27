package team.pacify.bookeet.ui.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.utils.Resource

class ReminderViewModel : ViewModel() {

    private val _reminders = MutableLiveData<Resource<List<Sale>>>()
    val reminders: LiveData<Resource<List<Sale>>> = _reminders

    fun getReminders() {
        viewModelScope.launch {
            _reminders.postValue(Resource.Loading())
            delay(1000)
            _reminders.postValue(
                Resource.Success(
                    emptyList()
                )
            )
        }
    }

}