package team.pacify.bookeet.data.dao.finance

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.utils.DbConstants
import javax.inject.Inject

class FirebaseAccountDao @Inject constructor(
    private val fStore: FirebaseFirestore,
) : AccountDao {

    override suspend fun addAccount(account: Account): Account {
        val ref = fStore.collection(DbConstants.ACCOUNTS_PATH).document()
        account.id = ref.id
        ref.set(account).await()
        return account
    }

    override suspend fun deleteAccount(account: Account) {
        fStore.collection(DbConstants.ACCOUNTS_PATH)
            .document(account.id)
            .delete().await()
    }

    override suspend fun updateAccount(account: Account): Account {
        fStore.collection(DbConstants.ACCOUNTS_PATH)
            .document(account.id)
            .set(account)
            .await()
        return account
    }

    override suspend fun getAccount(accountId: String): Account {
        val doc = fStore.collection(DbConstants.ACCOUNTS_PATH)
            .document(accountId).get().await()
        val account = doc.toObject<Account>()
        return account ?: throw Exception("No account found with ID")
    }

    override suspend fun getAllAccounts(userId: String): List<Account> {
        val doc = fStore.collection(DbConstants.ACCOUNTS_PATH)
        val query = doc.whereEqualTo("userId", userId)
        val entries = ArrayList<Account>(1)
        val result = query.get().await()

        for (r in result.documents) {
            val account = r.toObject<Account>()
            if (account != null) {
                entries.add(account)
            }
        }
        return entries
    }

}