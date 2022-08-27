package team.pacify.bookeet.data.models.util

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Reminder(
    @PrimaryKey
    val id: Int,
    val message: String = "",
    val isPeriodic: Boolean = false,
    val durationSecs: Int = 0,
    val setTime: Date = Calendar.getInstance().time,
    val dueTime: Date = Calendar.getInstance().time,
)
