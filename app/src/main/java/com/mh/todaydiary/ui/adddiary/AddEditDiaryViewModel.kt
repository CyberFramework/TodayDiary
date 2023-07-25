package com.mh.todaydiary.ui.adddiary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mh.todaydiary.data.repository.Diary
import com.mh.todaydiary.data.repository.DiaryRepository
import com.mh.todaydiary.data.repository.WorkResult
import com.mh.todaydiary.domain.DiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class AddEditDiaryUiState(
    val date: Date = Date(),
    var context: List<String> = listOf(""),
    val tag: List<String> = emptyList(),
    val loc: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)


@HiltViewModel
class AddEditDiaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addDiaryUseCase: DiaryUseCase,
    private val diaryRepository: DiaryRepository
): ViewModel() {
    private val timeId: Long = checkNotNull(savedStateHandle["time"])

    private val _uiState = MutableStateFlow(AddEditDiaryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (timeId != 0L) {
            loadDiary(timeId)
        }
    }

    private fun loadDiary(time: Long) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = diaryRepository.getDiaryFlow(time).first()
            if (result !is WorkResult.Success || result.data == null) {
                _uiState.update { it.copy(isLoading = false) }
            } else {
                val diary = result.data
                _uiState.update {
                    it.copy(
                        date = diary.date,
                        context = diary.context,
                        tag = diary.tag,
                        loc = diary.loc,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun addContent(content: String) {
        _uiState.update {
            val addedList = it.context.toMutableList().apply { add(content) }
            it.copy(context = addedList)
        }
    }

    fun removeContent(index: Int) {
        _uiState.update {
            val removedList = it.context.toMutableList().apply { removeAt(index) }
            it.copy(context = removedList)
        }
    }

    fun updateContent(index: Int, content: String) {
        _uiState.update {
            val updatedList = it.context.toMutableList()
            updatedList[index] = content
            it.copy(context = updatedList)
        }
    }

    fun saveDiary() {
        viewModelScope.launch {
            addDiaryUseCase(
                Diary(
                    time = System.currentTimeMillis(),
                    date = _uiState.value.date,
                    context = _uiState.value.context,
                    tag = _uiState.value.tag,
                    loc = _uiState.value.loc
                )
            )
        }
    }
}