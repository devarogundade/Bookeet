package team.pacify.bookeet.data

import androidx.room.Database
import androidx.room.RoomDatabase
import team.pacify.bookeet.data.dao.util.ReminderDao
import team.pacify.bookeet.data.models.util.Reminder

@Database(entities = [Reminder::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao (): ReminderDao
}