package com.mh.todaydiary.ui.adddiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mh.todaydiary.data.repository.Diary
import com.mh.todaydiary.domain.DiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val addDiaryUseCase: DiaryUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(AddEditDiaryUiState())
    val uiState = _uiState.asStateFlow()

    fun loadDiary(diary: Diary) {
        _uiState.update { it.copy(date = diary.date, context = diary.context.toMutableList(), tag = diary.tag, loc = diary.loc) }
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