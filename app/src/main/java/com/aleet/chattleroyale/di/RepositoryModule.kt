package com.squadsandshots_android.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.squadsandshots_android.localStorage.BasePreferences
import com.squadsandshots_android.localStorage.LocalStorageInterface
import com.squadsandshots_android.localStorage.Preferences
import com.squadsandshots_android.localStorage.SharedPreferencesStorage
import com.squadsandshots_android.repositories.DatabaseAuthRepoInterface
import com.squadsandshots_android.repositories.DatabaseRepositoryInterface
import com.squadsandshots_android.repositories.FirebaseAuthRepo
import com.squadsandshots_android.repositories.FirebaseRepo
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

}
