package team.pacify.bookeet.di

import android.content.Context
import com.bumptech.glide.Glide
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.ocpsoft.prettytime.PrettyTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModules {

    @Provides
    @Singleton
    fun requestManager(@ApplicationContext context: Context) = Glide.with(context)

    @Provides
    @Singleton
    fun prettyTime() = PrettyTime()

}