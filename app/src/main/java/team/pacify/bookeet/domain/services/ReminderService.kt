package team.pacify.bookeet.domain.services

import team.pacify.bookeet.data.models.util.Reminder

interface ReminderService {
    suspend fun addReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
}