package team.pacify.bookeet.data.dao.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageDao @Inject constructor(
    private val fStorage: FirebaseStorage
) : StorageDao {
    override suspend fun upload(path: String, id: String, uri: Uri): String {
        val ref = fStorage.reference
        ref.child(path).child(id)
            .putFile(uri)
            .await()
        return ref.child(path).child(id)
            .downloadUrl.await()
            .toString()
    }

    override suspend fun delete(path: String, id: String) {
        fStorage.reference.child(path).child(id).delete()
    }
}