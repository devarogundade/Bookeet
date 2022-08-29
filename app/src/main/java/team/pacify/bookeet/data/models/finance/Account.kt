package team.pacify.bookeet.data.models.finance

import com.google.gson.annotations.SerializedName
import team.pacify.bookeet.data.models.Entry
import java.util.*

data class Account(
    override var id: String = "",
    override var userId: String = "",
    var balance: Double = 0.0,
    @SerializedName("vnuban")
    var accNo: String = "",
    @SerializedName("bank_name")
    var bankName: String? = null,
    @SerializedName("bank_code")
    val bankCode: String? = null,
    var timeStamp: Date = Calendar.getInstance().time,
    val bvn: String? = null,
    val currency: String? = null
) : Entry
