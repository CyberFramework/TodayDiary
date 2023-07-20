package com.mh.todaydiary.data.source.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mh.todaydiary.data.repository.Diary
import java.util.Date

@Entity(tableName = "diaries")
data class DiaryEntity(
    @PrimaryKey var time: Long,
    var date: Date,
    var context: List<String>,
    var loc: List<String>,
    var tag: List<String>,
) {
    fun toDiary(): Diary {
        return Diary(
            time = time,
            date = date,
            context = context,
            loc = loc,
            tag = tag,
        )
    }
}

fun Diary.toDiaryEntity(): DiaryEntity {
    return DiaryEntity(
        time = time,
        date = date,
        context = context,
        loc = loc,
        tag = tag,
    )
}