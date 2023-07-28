package com.mh.todaydiary.ui.diarylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mh.todaydiary.R
import com.mh.todaydiary.data.repository.Diary
import com.mh.todaydiary.ui.custom.CalendarWithAdjacentMonths
import com.mh.todaydiary.ui.diarylist.ui.theme.Pastel
import java.time.LocalDate

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
        Column(Modifier.padding(defaultPadding)) {
            CalendarWithAdjacentMonths()

            if (diaryList.diaries.isNotEmpty()) {
                DiaryList(
                    diaries = diaryList.diaries,
                    onEditItem = editDiary,
                    onDeleteItem = { deleteDiaryTime = it }
                )
            }

        }
    }

    if (deleteDiaryTime > 0) {
        DeleteDialog(
            onConfirm = { diaryListViewModel.deleteDiary(deleteDiaryTime) },
            onDismiss = { deleteDiaryTime = 0 }
        )
    }
}

@Composable
private fun DiaryList(diaries: List<Diary>, onEditItem: (Long) -> Unit, onDeleteItem: (Long) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(5.dp)
    ) {
        items(diaries) {
            DiaryPreviewIcon(
                color = Pastel.colorArray[(it.time % 10).toInt()],
                contents = it.context,
                onClick = { onEditItem(it.time) },
                onLongClick = { onDeleteItem(it.time) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DiaryPreviewIcon(contents: List<String>, color: Color, onClick: () -> Unit, onLongClick: () -> Unit) {
    var imageUri = ""
    var firstText = ""

    contents.forEach {
        if (it.startsWith("content://") && imageUri.isEmpty()) {
            imageUri = it
        } else if (firstText.isEmpty()) {
            firstText = it
        } else if (imageUri.isNotEmpty() && firstText.isNotEmpty()) {
            return@forEach
        }
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(color)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(model = imageUri, contentDescription = firstText, contentScale = ContentScale.Crop)
        Text(text = firstText, overflow = TextOverflow.Ellipsis)
    }
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