package com.mh.todaydiary.domain

import com.mh.todaydiary.data.repository.Diary
import com.mh.todaydiary.data.repository.DiaryRepository
import java.lang.Exception
import javax.inject.Inject

class DiaryUseCase @Inject constructor(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(diary: Diary) {
        if (diary.context.isEmpty()) {
            throw Exception("Please input context")
        }

        diaryRepository.addDiary(diary)
    }
}