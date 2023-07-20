package com.mh.todaydiary.data.source.local.room

import com.mh.todaydiary.data.repository.Diary
import com.mh.todaydiary.data.repository.WorkResult
import com.mh.todaydiary.data.source.local.LocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalDataSource(
    private val diaryDao: DiaryDao,
) : LocalDataSource {
    override fun getDiariesFlow(): Flow<WorkResult<List<Diary>>> {
        return diaryDao.observeDiaries().map {
            WorkResult.Success(it.map { diaryEntity -> diaryEntity.toDiary() })
        }
    }

    override suspend fun addDiary(diary: Diary) {
        diaryDao.insertDiary(diary.toDiaryEntity())
    }

    override suspend fun deleteDiary(time: Long) {
        diaryDao.deleteDiary(time)
    }
}