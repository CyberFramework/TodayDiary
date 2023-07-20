package com.mh.todaydiary.data.source.remote

import com.mh.todaydiary.data.repository.Diary

interface RemoteDataSource {
    suspend fun getDiaries(): List<Diary>
    suspend fun addDiary(diary: Diary): Diary
    suspend fun deleteDiary(time: Long)
}