package team.pacify.bookeet.data.dao.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.utils.Resource

interface AccountDao {
    suspend fun addAccount(account: Account): Account

    suspend fun deleteAccount(account: Account)

    suspend fun updateAccount(account: Account): Account

    suspend fun getAccount(accountId: String): Account

    suspend fun getAllAccounts(userId: String): Flow<Resource<List<Account>>>
}