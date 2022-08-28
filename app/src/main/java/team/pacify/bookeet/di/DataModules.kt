package team.pacify.bookeet.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.pacify.bookeet.data.dao.finance.*
import team.pacify.bookeet.data.dao.inventory.FirebaseProductDao
import team.pacify.bookeet.data.dao.inventory.FirebaseServiceDao
import team.pacify.bookeet.data.dao.inventory.ProductDao
import team.pacify.bookeet.data.dao.inventory.ServiceDao
import team.pacify.bookeet.data.dao.messaging.ChatDao
import team.pacify.bookeet.data.dao.messaging.FirebaseChatDao
import team.pacify.bookeet.data.dao.person.*

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModules {

    @Binds
    abstract fun productDao(productDao: FirebaseProductDao): ProductDao

    @Binds
    abstract fun serviceDao(serviceDao: FirebaseServiceDao): ServiceDao

    @Binds
    abstract fun accountDao(accountDao: FirebaseAccountDao): AccountDao

    @Binds
    abstract fun invoiceDao(invoiceDao: FirebaseInvoiceDao): InvoiceDao

    @Binds
    abstract fun saleDao(saleDao: FirebaseSaleDao): SaleDao

    @Binds
    abstract fun transactionDao(transactionDao: FirebaseTransactionDao): TransactionDao

    @Binds
    abstract fun customerDao(customerDao: FirebaseCustomerDao): CustomerDao

    @Binds
    abstract fun supplierDao(supplierDao: FirebaseSupplierDao): SupplierDao

    @Binds
    abstract fun userDao(userDao: FirebaseUserDao): UserDao

    @Binds
    abstract fun chatDao(chatDao: FirebaseChatDao): ChatDao

}