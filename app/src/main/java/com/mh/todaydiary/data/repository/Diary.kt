package com.mh.todaydiary.data.repository

import java.util.Date

data class Diary(
    val time: Long,
    val date: Date,
    val context: List<String>,
    val loc: List<String>,
    val tag: List<String>
)