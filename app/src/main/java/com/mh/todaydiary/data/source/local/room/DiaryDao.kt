package com.mh.todaydiary.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diaries")
    fun observeDiaries(): Flow<List<DiaryEntity>>

    @Query("SELECT * FROM diaries WHERE time = :time")
    fun observeDiary(time: Long): Flow<DiaryEntity>

    @Query("SELECT * FROM diaries WHERE time >= :start AND time <= :end")
    suspend fun getDiaryByDuration(start: Long, end: Long): List<DiaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(planet: DiaryEntity)

    @Query("SELECT * FROM diaries WHERE time = :time")
    suspend fun getDiary(time: Long): DiaryEntity?

    @Query("DELETE FROM diaries WHERE time = :time")
    suspend fun deleteDiary(time: Long)

    @Query("DELETE FROM diaries")
    suspend fun deleteAllDiaries()

    @Transaction
    suspend fun setDiaries(diaries: List<DiaryEntity>) {
        deleteAllDiaries()
        diaries.forEach { insertDiary(it) }
    }
}