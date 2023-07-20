package com.mh.todaydiary.data.repository

import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    fun getDiariesFlow(): Flow<WorkResult<List<Diary>>>
    suspend fun addDiary(diary: Diary)
    suspend fun deleteDiary(time: Long)
}