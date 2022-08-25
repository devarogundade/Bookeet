package team.pacify.bookeet.data.models.util

import java.util.*

data class Reminder(
    val message: String = "",
    val isPeriodic: Boolean = false,
    val durationSecs: Int = 0,
    val setTime: Date = Calendar.getInstance().time,
    val dueTime: Date = Calendar.getInstance().time,
)
