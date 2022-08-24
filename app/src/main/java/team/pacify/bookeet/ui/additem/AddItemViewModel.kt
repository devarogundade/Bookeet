package team.pacify.bookeet.ui.additem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import team.pacify.bookeet.utils.Resource

class AddItemViewModel : ViewModel() {

    private val _save = MutableLiveData<Resource<Boolean>>()
    val save: LiveData<Resource<Boolean>> = _save

    fun saveProduct() {

    }

    fun saveService() {

    }

}