package team.pacify.bookeet.data.dao.util

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import team.pacify.bookeet.data.models.util.Reminder

@Dao
interface ReminderDao {
    @Insert
    fun addReminders(vararg reminders: Reminder)
    @Delete
    fun deleteReminder(reminder: Reminder)
}