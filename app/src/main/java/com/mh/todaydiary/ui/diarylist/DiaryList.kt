package com.mh.todaydiary.ui.diarylist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mh.todaydiary.data.repository.Diary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    addDiary: () -> Unit,
    editDiary: (Long) -> Unit,
    diaryListViewModel: DiaryListViewModel = hiltViewModel(),
) {
    val diaryList by diaryListViewModel.uiState.collectAsState()

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = addDiary) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }) { defaultPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(defaultPadding)) {
            items(diaryList.diaries) {
                DiaryPreviewIcon(it) { editDiary(it.time) }
            }
        }
    }
}

@Composable
fun DiaryPreviewIcon(diary: Diary, onClick: () -> Unit) {
    Text(
        text = diary.context.joinToString(),
        modifier = Modifier.clickable { onClick() }
    )
}