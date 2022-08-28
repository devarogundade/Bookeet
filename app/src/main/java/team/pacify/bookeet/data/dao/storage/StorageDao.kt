package team.pacify.bookeet.data.dao.storage

import android.net.Uri

interface StorageDao {
    suspend fun upload(path: String, id: String, uri: Uri): String
    suspend fun delete(path: String, id: String)
}