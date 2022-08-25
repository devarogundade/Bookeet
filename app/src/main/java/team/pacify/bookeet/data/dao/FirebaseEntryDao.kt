package team.pacify.bookeet.data.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.Entry
import javax.inject.Inject

abstract class FirebaseEntryDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) {
    abstract val rootPath: String

    suspend fun addEntry(entry: Entry): Entry {
        val ref = fStore.collection(rootPath).document()
        entry.id = ref.id
        ref.set(entry).await()
        return entry
    }

    suspend fun deleteEntry(entry: Entry) {
        fStore.collection(rootPath)
            .document(entry.id)
            .delete().await()
    }

    suspend fun updateEntry(entry: Entry): Entry {
        fStore.collection(rootPath)
            .document(entry.id)
            .set(entry)
            .await()
        return entry
    }

    suspend fun getEntry(entryId: String): Entry {
        val doc = fStore.collection(rootPath)
            .document(entryId).get().await()
        val entry = doc.toObject<Entry>()
        return entry ?: throw Exception("No entry found with ID")
    }

    suspend fun getAllEntities(userId: String): List<Entry> {
        val doc = fStore.collection(rootPath)
        val query = doc.whereEqualTo("userId", userId)
        val entries = ArrayList<Entry>(1)
        val result = query.get().await()

        for (r in result.documents) {
            val entry = r.toObject<Entry>()
            if (entry != null) {
                entries.add(entry)
            }
        }
        return entries
    }

}