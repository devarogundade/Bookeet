package team.pacify.bookeet.data.models.person

import com.google.firebase.firestore.ServerTimestamp
import team.pacify.bookeet.data.models.Entry
import team.pacify.bookeet.domain.models.Location
import java.util.*

data class Supplier(
    @ServerTimestamp
    var timeStamp: Date? = null,
    val name: String,
    val details: String,
    val address : String,
    val email : String,
    val location : Location,
    var id: String,

 )