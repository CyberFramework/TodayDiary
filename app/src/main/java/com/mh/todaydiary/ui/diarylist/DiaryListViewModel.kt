package com.mh.todaydiary.ui.diarylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mh.todaydiary.data.repository.Diary
import com.mh.todaydiary.data.repository.DiaryRepository
import com.mh.todaydiary.data.repository.WorkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiaryListUiState(
    val diaries: List<Diary> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
) : ViewModel() {
    private val diaries = diaryRepository.getDiariesFlow()
    private var start: Long = 0
    private var end: Long = Long.MAX_VALUE

    val uiState = diaries.map {
        when (it) {
            is WorkResult.Error -> DiaryListUiState(isError = true)
            is WorkResult.Loading -> DiaryListUiState(isLoading = true)
            is WorkResult.Success -> DiaryListUiState(diaries = it.data.filter { diary -> diary.time in start..end })
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DiaryListUiState(isLoading = true)
    )

    fun deleteDiary(time: Long) {
        viewModelScope.launch {
            diaryRepository.deleteDiary(time)
        }
    }

    fun getDiariesByDuration(s: Long, e: Long) {
        viewModelScope.launch {
            start = s * 1000
            end = e * 1000
            diaryRepository.refreshDiaries()
        }
    }
}