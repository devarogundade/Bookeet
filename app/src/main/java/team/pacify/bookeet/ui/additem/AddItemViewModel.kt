package team.pacify.bookeet.ui.additem

import androidx.lifecycle.ViewModel
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.data.models.inventory.Service
import team.pacify.bookeet.utils.Resource

class AddItemViewModel : ViewModel() {

    fun addProduct(product: Product): Resource<Boolean> {
        return Resource.Success(true)
    }

    fun addService(service: Service): Resource<Boolean> {
        return Resource.Success(true)
    }

}