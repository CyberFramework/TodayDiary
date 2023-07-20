package com.mh.todaydiary.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.mh.todaydiary.data.repository.DefaultDiaryRepository
import com.mh.todaydiary.data.repository.DiaryRepository
import com.mh.todaydiary.data.source.local.LocalDataSource
import com.mh.todaydiary.data.source.local.room.AppDatabase
import com.mh.todaydiary.data.source.local.room.RoomLocalDataSource
import com.mh.todaydiary.data.source.local.room.StringListTypeConverter
import com.mh.todaydiary.data.source.remote.ApiRemoteDataSource
import com.mh.todaydiary.data.source.remote.RemoteDataSource
import com.mh.todaydiary.domain.DiaryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideAddDiaryUseCase(
        repository: DiaryRepository,
    ): DiaryUseCase {
        return DiaryUseCase(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDiaryRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
    ): DiaryRepository {
        return DefaultDiaryRepository(localDataSource, remoteDataSource)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return ApiRemoteDataSource()
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        database: AppDatabase,
    ): LocalDataSource {
        return RoomLocalDataSource(database.diaryDao())
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context, gson: Gson): AppDatabase {
        return Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, "diaries.db")
            .addTypeConverter(StringListTypeConverter(gson))
            .build()
    }
}
