package team.pacify.bookeet.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import team.pacify.bookeet.data.AppDatabase
import team.pacify.bookeet.data.dao.*
import team.pacify.bookeet.data.dao.finance.*
import team.pacify.bookeet.data.dao.inventory.FirebaseServiceDao
import team.pacify.bookeet.data.dao.inventory.ServiceDao
import team.pacify.bookeet.data.dao.person.FirebaseSupplierDao
import team.pacify.bookeet.data.dao.person.SupplierDao
import javax.inject.Singleton

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


}