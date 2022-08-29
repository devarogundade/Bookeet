package team.pacify.bookeet.data.dao.finance

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.clients.FsiClient
import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseAccountDao @Inject constructor(
    private val fStore: FirebaseFirestore,
    private val fsiClient: FsiClient,
) : AccountDao {

    override suspend fun addAccount(account: Account): Account {
        fsiClient.createVirtualAccount(account.userId)
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

    override suspend fun getAllAccounts(userId: String): Flow<Resource<List<Account>>> {
        return callbackFlow {
            val doc = fStore.collection(DbConstants.ACCOUNTS_PATH)
            doc.whereEqualTo("userId", userId)
                .addSnapshotListener { value, error ->
                    if (error != null)
                        launch {
                            send(
                                Resource.Error(
                                    error.localizedMessage ?: "Something went wrong"
                                )
                            )
                        }
                    if (value != null)
                        launch { send(Resource.Success(value.toObjects())) }
                }
            awaitClose {
                cancel()
            }
        }
    }

}