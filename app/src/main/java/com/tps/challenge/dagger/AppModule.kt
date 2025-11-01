package com.tps.challenge.dagger

import com.tps.challenge.StoreRepository
import android.app.Application
import com.tps.challenge.TCApplication
import com.tps.challenge.database.AppDatabase
import com.tps.challenge.database.StoreDao
import com.tps.challenge.network.TPSCoroutineService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: TCApplication) {
    @Provides
    @Singleton
    fun getApplication(): Application {
        return application
    }

    /**
     * Provides an instance of [AppDatabase].
     */
    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getDatabase(application)
    }

    @Provides
    @Singleton
    fun providesDao(database: AppDatabase): StoreDao{
        return database.storeDao()
    }

    @Provides
    @Singleton
    fun provideStoreRepository(
        coroutineApiService: TPSCoroutineService,
        storeDao: StoreDao
    ): StoreRepository {
        return StoreRepository(coroutineApiService, storeDao)
    }
}
