package team.pacify.bookeet.ui.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.pacify.bookeet.data.models.util.Reminder
import team.pacify.bookeet.utils.Resource

class ReminderViewModel : ViewModel() {

    private val _reminders = MutableLiveData<Resource<List<Reminder>>>()
    val reminders: LiveData<Resource<List<Reminder>>> = _reminders

    fun getReminders() {
        viewModelScope.launch {
            _reminders.postValue(Resource.Loading())
            delay(400)
            _reminders.postValue(
                Resource.Success(
                    listOf(
                        Reminder(
                            1,
                            "Send money to Chisom",
                            false,
                            60
                        )
                    )
                )
            )
        }
    }

}