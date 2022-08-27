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
import team.pacify.bookeet.data.dao.FirebaseProductDao
import team.pacify.bookeet.data.dao.ProductDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModules {

    @Binds
    abstract fun productDao(productDao: FirebaseProductDao): ProductDao

    @Provides
    @Singleton
    fun appDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "bookeet"
        ).build()
    }
}