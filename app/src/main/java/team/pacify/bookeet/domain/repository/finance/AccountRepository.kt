package team.pacify.bookeet.domain.repository.finance

import kotlinx.coroutines.flow.Flow
import team.pacify.bookeet.data.dao.finance.AccountDao
import team.pacify.bookeet.data.models.finance.Account
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val dao: AccountDao
) {
    suspend fun createAccount(userId: String): Resource<Account?> {
        return try {
            val account = dao.createAccount(userId)?.data
            Resource.Success(account)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

    suspend fun getAccount(userId: String): Flow<Resource<Account?>> {
        return dao.getAccount(userId)
    }

}