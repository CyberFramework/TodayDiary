package com.mh.todaydiary.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diaries")
    fun observeDiaries(): Flow<List<DiaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(planet: DiaryEntity)

    @Query("DELETE FROM diaries WHERE time = :time")
    suspend fun deleteDiary(time: Long)

    @Query("DELETE FROM diaries")
    suspend fun deleteAllDiaries()
}