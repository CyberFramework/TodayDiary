package com.mh.todaydiary.data.source.local.room

import androidx.room.Database
import androidx.room.ProvidedTypeConverter
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import java.util.Date

@Database(entities = [DiaryEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    value = [
        Converters::class,
        StringListTypeConverter::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
}

object Converters {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?) = date?.time
}

@ProvidedTypeConverter
class StringListTypeConverter(private val gson: Gson) {
    @TypeConverter
    fun listToJson(value: List<String>): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String> {
        return gson.fromJson(value, Array<String>::class.java).toList()
    }
}