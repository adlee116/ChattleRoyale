package com.aleet.chattleroyale.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.aleet.chattleroyale.localStorage.BasePreferences
import com.aleet.chattleroyale.localStorage.LocalStorageInterface
import com.aleet.chattleroyale.localStorage.Preferences
import com.aleet.chattleroyale.localStorage.SharedPreferencesStorage
import com.aleet.chattleroyale.localStorage.UserDao
import com.aleet.chattleroyale.localStorage.UserDatabase
import com.aleet.chattleroyale.repositories.DatabaseAuthRepoInterface
import com.aleet.chattleroyale.repositories.DatabaseRepositoryInterface
import com.aleet.chattleroyale.repositories.FirebaseAuthRepo
import com.aleet.chattleroyale.repositories.FirebaseRepo
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthDataRepo(): DatabaseAuthRepoInterface {
        return FirebaseAuthRepo()
    }
    @Provides
    @Singleton
    fun provideFirebaseDataRepo(): DatabaseRepositoryInterface {
        return FirebaseRepo()
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Preferences.FILE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun sharedPreferenceStorage(@ApplicationContext context: Context): LocalStorageInterface {
        return SharedPreferencesStorage(providePreferences(context), provideGson())
    }

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): BasePreferences {
        return Preferences(provideSharedPreference(context), provideGson())
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson().newBuilder().create()
    }

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: UserDatabase): UserDao {
        return database.userDao()
    }

}

