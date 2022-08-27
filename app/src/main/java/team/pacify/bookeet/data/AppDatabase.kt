package team.pacify.bookeet.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import team.pacify.bookeet.data.dao.util.ReminderDao
import team.pacify.bookeet.data.models.util.Reminder

@Database(entities = [Reminder::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao (): ReminderDao
}