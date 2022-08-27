package team.pacify.bookeet.data.dao.finance

import team.pacify.bookeet.data.models.finance.Account

interface AccountDao {
    suspend fun addAccount(account: Account): Account

    suspend fun deleteAccount(account: Account)

    suspend fun updateAccount(account: Account): Account

    suspend fun getAccount(accountId: String): Account

    suspend fun getAllAccounts(userId: String): List<Account>
}