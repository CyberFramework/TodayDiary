package com.mh.todaydiary.ui.diarylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.mh.todaydiary.R
import com.mh.todaydiary.data.repository.Diary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    addDiary: () -> Unit,
    editDiary: (Long) -> Unit,
    diaryListViewModel: DiaryListViewModel = hiltViewModel(),
) {
    val diaryList by diaryListViewModel.uiState.collectAsState()
    var deleteDiaryTime by remember { mutableStateOf(0L) }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = addDiary) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }) { defaultPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(defaultPadding)
        ) {
            items(diaryList.diaries) {
                DiaryPreviewIcon(it,
                    onClick = { editDiary(it.time) },
                    onLongClick = { deleteDiaryTime = it.time }
                )
            }
        }

        if (deleteDiaryTime > 0) {
            DeleteDialog(
                onConfirm = { diaryListViewModel.deleteDiary(deleteDiaryTime) },
                onDismiss = { deleteDiaryTime = 0 }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DiaryPreviewIcon(diary: Diary, onClick: () -> Unit, onLongClick: () -> Unit) {
    Text(
        text = diary.context.joinToString(),
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
    )
}

@Composable
private fun DeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) { Text(text = stringResource(id = R.string.delete)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(text = stringResource(id = R.string.cancel)) } },
        text = { Text(text = stringResource(id = R.string.are_you_delete_diary)) },
    )
}