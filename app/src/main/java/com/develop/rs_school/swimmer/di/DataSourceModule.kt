package com.develop.rs_school.swimmer.di

import android.content.Context
import androidx.room.Room
import com.develop.rs_school.swimmer.data.DataSource
import com.develop.rs_school.swimmer.data.database.CustomerDao
import com.develop.rs_school.swimmer.data.database.DatabaseDataSource
import com.develop.rs_school.swimmer.data.database.LessonDao
import com.develop.rs_school.swimmer.data.database.SwimmerDatabase
import com.develop.rs_school.swimmer.data.network.NetworkDataSource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

// Tells Dagger this is a Dagger module
@Module
object DataSourceModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalDataSource

    @JvmStatic
    @Singleton
    @Provides
    @LocalDataSource
    fun provideLocalStorage(database: SwimmerDatabase, ioDispatcher: CoroutineDispatcher): DataSource {
        // Whenever Dagger needs to provide an instance of type Storage,
        // this code (the one inside the @Provides method) will be run.
        return DatabaseDataSource(database.lessonDatabaseDao, database.customerDatabaseDao, ioDispatcher)
    }

    @JvmStatic
    @Singleton
    @Provides
    @RemoteDataSource
    fun provideRemoteStorage(): DataSource {
        // Whenever Dagger needs to provide an instance of type Storage,
        // this code (the one inside the @Provides method) will be run.
        return NetworkDataSource()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): SwimmerDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SwimmerDatabase::class.java,
            "swimmer_database"
        ).build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}