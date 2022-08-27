package team.pacify.bookeet.domain.services

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import team.pacify.bookeet.data.models.util.Reminder

class AndroidReminderService constructor (@ApplicationContext context: Context) : ReminderService {
    override suspend fun addReminder(reminder: Reminder) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        TODO("Not yet implemented")
    }
}