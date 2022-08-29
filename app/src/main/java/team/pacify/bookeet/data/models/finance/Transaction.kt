package team.pacify.bookeet.data.models.finance

import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.SerializedName
import team.pacify.bookeet.data.models.Entry
import java.util.*

data class Transaction(
    @ServerTimestamp
    var timeStamp: Date? = null,
    val narration: String = "",
    @SerializedName("nuban")
    val accountNumber: Long = -1,
    @SerializedName("account_name")
    val accountName: String = "",
    @SerializedName("bank_code")
    val bankCode: String = "",
    val amount: Double = 0.0,
    val fee: Double = 0.0,
    val date: Date? = Calendar.getInstance().time,
    val status: String? = null,
    @SerializedName("transaction_type")
    val type: String? = null,
    @SerializedName("unique_reference")
    override var id: String = "",
    override var userId: String = "",
) : Entry