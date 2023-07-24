package com.mh.todaydiary.data.source.local

import com.mh.todaydiary.data.repository.Diary
import com.mh.todaydiary.data.repository.WorkResult
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getDiariesFlow(): Flow<WorkResult<List<Diary>>>
    fun getDiaryFlow(time: Long): Flow<WorkResult<Diary?>>
    suspend fun addDiary(diary: Diary)
    suspend fun deleteDiary(time: Long)
}