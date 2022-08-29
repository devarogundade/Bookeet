package team.pacify.bookeet.data.dao.finance

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import team.pacify.bookeet.data.clients.FsiClient
import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.data.responses.FsiResponse
import team.pacify.bookeet.utils.DbConstants
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class FirebaseAccountDao @Inject constructor(
    private val fStore: FirebaseFirestore,
    private val fsiClient: FsiClient,
) : AccountDao {

    override suspend fun createAccount(userId: String): FsiResponse<Account>? {
        val request = fsiClient.createVirtualAccount(userId)
        val response = request.body()
        response?.data?.apply {
            updateAccount(
                Account(
                    id = userId,
                    userId = userId,
                    bankName = bankName,
                    bankCode = bankCode,
                    currency = currency,
                    accNo = accNo
                )
            )
        }
        return response
    }

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

    override suspend fun getAccount(accountId: String): Flow<Resource<Account?>> {
        return callbackFlow {
            fStore.collection(DbConstants.ACCOUNTS_PATH)
                .document(accountId)
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
                        launch { send(Resource.Success(value.toObject())) }

                }
            awaitClose {
                cancel()
            }
        }
    }

}