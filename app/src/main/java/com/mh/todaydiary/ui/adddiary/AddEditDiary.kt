package com.mh.todaydiary.ui.adddiary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDiaryScreen(
    addEditDiaryViewModel: AddEditDiaryViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val diary by addEditDiaryViewModel.uiState.collectAsState()
    var text by remember { mutableStateOf(diary.context.joinToString()) }

    Column(Modifier.fillMaxSize()) {
        TextField(value = text, onValueChange = {
            text = it
            diary.context = listOf(text)
            println(diary.context)
            println(addEditDiaryViewModel.uiState.value.context)
        })

        Button(onClick = {
            addEditDiaryViewModel.saveDiary()
            onComplete()
        }) {
            Text(text = "작성하기")
        }
    }
}