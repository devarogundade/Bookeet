package team.pacify.bookeet.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.pacify.bookeet.data.dao.FirebaseProductDao
import team.pacify.bookeet.data.dao.ProductDao

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModules {

    @Binds
    abstract fun productDao(productDao: FirebaseProductDao): ProductDao
}