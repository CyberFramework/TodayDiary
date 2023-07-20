package com.mh.todaydiary.data.source.remote

import com.mh.todaydiary.data.repository.Diary
import java.util.Date
import javax.inject.Inject

class ApiRemoteDataSource @Inject constructor() : RemoteDataSource  {
    override suspend fun getDiaries(): List<Diary> {
        // TODO("서버 요청")
        return emptyList()
    }

    override suspend fun addDiary(diary: Diary): Diary {
        // TODO("서버 요청")
        return Diary(0, Date(), emptyList(), emptyList(), emptyList())
    }

    override suspend fun deleteDiary(time: Long) {
        // TODO("서버 요청")
    }
}