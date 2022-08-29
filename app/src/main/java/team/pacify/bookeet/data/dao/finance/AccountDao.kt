package team.pacify.bookeet.data.dao.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.data.responses.FsiResponse
import team.pacify.bookeet.utils.Resource

interface AccountDao {
    suspend fun createAccount(userId: String): FsiResponse<Account>?

    suspend fun addAccount(account: Account): Account

    suspend fun deleteAccount(account: Account)

    suspend fun updateAccount(account: Account): Account

    suspend fun getAccount(accountId: String): Flow<Resource<Account?>>
}