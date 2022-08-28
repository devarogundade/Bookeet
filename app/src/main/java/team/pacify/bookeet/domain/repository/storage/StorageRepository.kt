package team.pacify.bookeet.domain.repository.storage

import android.net.Uri
import team.pacify.bookeet.data.dao.storage.StorageDao
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storageDao: StorageDao
) {

    suspend fun uploadFile(path: String, id: String, uri: Uri): Resource<String> {
        return try {
            Resource.Success(storageDao.upload(path, id, uri))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun deleteFile(path: String, id: String): Resource<Boolean> {
        return try {
            storageDao.delete(path, id)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

}