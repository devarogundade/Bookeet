package team.pacify.bookeet.data.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.Entry
import team.pacify.bookeet.utils.DbConstants
import javax.inject.Inject

class FirebaseEntryDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) {

    suspend fun addEntry(entry: Entry): Entry {
        val ref = fStore.collection(DbConstants.ENTRIES_PATH).document()
        entry.id = ref.id
        ref.set(entry).await()
        return entry
    }

    suspend fun deleteEntry(entry: Entry) {
        fStore.collection(DbConstants.ENTRIES_PATH)
            .document(entry.id)
            .delete().await()
    }

    suspend fun updateEntry(entry: Entry): Entry {
        fStore.collection(DbConstants.ENTRIES_PATH)
            .document(entry.id)
            .set(entry)
            .await()
        return entry
    }

    suspend fun getEntry(entryId: String): Entry {
        val doc = fStore.collection(DbConstants.ENTRIES_PATH)
            .document(entryId).get().await()
        val entry = doc.toObject<Entry>()
        return entry ?: throw Exception("No entry found with ID")
    }

    suspend fun getAllEntries(userId: String): List<Entry> {
        val doc = fStore.collection(DbConstants.ENTRIES_PATH)
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