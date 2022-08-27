package team.pacify.bookeet.domain.services.api

import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.utils.Resource

interface FinanceService {
    suspend fun getCurrentAccount(userId: String): Resource<Account>
    suspend fun createAccount(userId: String): Resource<Account>
    suspend fun sendAmount(acc: Account, amount: Double): Resource<Transaction>
}

