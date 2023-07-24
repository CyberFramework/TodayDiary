package com.mh.todaydiary.data.repository

import com.mh.todaydiary.data.source.local.LocalDataSource
import com.mh.todaydiary.data.source.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultDiaryRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
): DiaryRepository {
    override fun getDiariesFlow(): Flow<WorkResult<List<Diary>>> {
        return localDataSource.getDiariesFlow()
    }

    override fun getDiaryFlow(time: Long): Flow<WorkResult<Diary?>> {
        return localDataSource.getDiaryFlow(time)
    }

    override suspend fun addDiary(diary: Diary) {
        remoteDataSource.addDiary(diary)
        localDataSource.addDiary(diary)
    }

    override suspend fun deleteDiary(time: Long) {
        localDataSource.deleteDiary(time)
        remoteDataSource.deleteDiary(time)
    }
}