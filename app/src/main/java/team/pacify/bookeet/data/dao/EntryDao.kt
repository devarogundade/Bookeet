package team.pacify.bookeet.data.dao

import team.pacify.bookeet.data.models.Entry
import kotlin.reflect.KClass

interface EntryDao<T> {
    suspend fun addEntry(entry: Entry): T

    suspend fun deleteEntry(entry: Entry)

    suspend fun updateEntry(entry: Entry): T

    suspend fun getEntry(entryId: String): T

    suspend fun getAllEntities(userId: String): List<T>
}