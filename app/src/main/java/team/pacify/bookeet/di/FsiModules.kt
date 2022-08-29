package team.pacify.bookeet.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import team.pacify.bookeet.data.clients.FsiClient
import team.pacify.bookeet.utils.FSIConstants.CONN_TIMEOUT
import team.pacify.bookeet.utils.FSIConstants.FSI_BASE_URL
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FsiModules {

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            client(okHttpClient)
            baseUrl(FSI_BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    @Provides
    @Singleton
    fun fsiClient(retrofit: Retrofit): FsiClient {
        return retrofit.create(FsiClient::class.java)
    }

}