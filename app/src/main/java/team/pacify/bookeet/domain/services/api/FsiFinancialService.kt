package team.pacify.bookeet.domain.services.api

import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.utils.Resource

class FsiFinancialService : FinanceService {
    override suspend fun getCurrentAccount(userId: String): Resource<Account> {
        TODO("Not yet implemented")
    }

    override suspend fun createAccount(userId: String): Resource<Account> {
        TODO("Not yet implemented")
    }

    override suspend fun sendAmount(acc: Account, amount: Double): Resource<Transaction> {
        TODO("Not yet implemented")
    }
}