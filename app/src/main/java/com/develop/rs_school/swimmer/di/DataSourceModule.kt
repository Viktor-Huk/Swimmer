package com.develop.rs_school.swimmer.di

import android.content.Context
import androidx.room.Room
import com.develop.rs_school.swimmer.data.DataSource
import com.develop.rs_school.swimmer.data.database.DatabaseDataSource
import com.develop.rs_school.swimmer.data.database.SwimmerDatabase
import com.develop.rs_school.swimmer.data.network.CustomDateTimeAdapter
import com.develop.rs_school.swimmer.data.network.NetworkDataSource
import com.develop.rs_school.swimmer.data.network.SwimmerApi
import com.develop.rs_school.swimmer.data.network.SwimmerApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

private const val BASE_URL = "https://mevis.s20.online/v2api/"

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
    fun provideLocalStorage(
        database: SwimmerDatabase,
        ioDispatcher: CoroutineDispatcher
    ): DataSource {
        // Whenever Dagger needs to provide an instance of type Storage,
        // this code (the one inside the @Provides method) will be run.
        return DatabaseDataSource(
            database.lessonDatabaseDao,
            database.customerDatabaseDao,
            ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    @RemoteDataSource
    fun provideRemoteStorage(swimmerApi: SwimmerApi): DataSource {
        // Whenever Dagger needs to provide an instance of type Storage,
        // this code (the one inside the @Provides method) will be run.
        return NetworkDataSource(swimmerApi)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSwimmerApi(): SwimmerApi {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(CustomDateTimeAdapter())
            .build()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
        val retrofitService: SwimmerApiService = retrofit.create(SwimmerApiService::class.java)
        return SwimmerApi(retrofitService)
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