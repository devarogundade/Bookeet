package team.pacify.bookeet.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModules {

    @Provides
    @Singleton
    fun firebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun firebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun firebaseFireStore() = FirebaseFirestore.getInstance()

}