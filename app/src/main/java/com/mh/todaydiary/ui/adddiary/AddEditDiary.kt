package com.mh.todaydiary.ui.adddiary

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun AddEditDiaryScreen(
    viewModel: AddEditDiaryViewModel = hiltViewModel(),
    onComplete: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val activityResultContracts = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.addContent(it.toString())
        }
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.weight(1f)) {
            itemsIndexed(uiState.context) { index, content ->
                ContentContainer(content = if (content.startsWith("content://")) content.toUri() else content) {
                    viewModel.removeContent(index)
                }
            }
        }

        Row {
            Button(onClick = {
                viewModel.saveDiary()
                onComplete()
            }) {
                Text(text = "작성하기")
            }

            Button(onClick = {
                activityResultContracts.launch("image/*")
            }) {
                Text(text = "이미지")
            }
            Button(onClick = {
                viewModel.addContent("")
            }) {
                Text(text = "텍스트")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentContainer(content: Any, onDelete: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = "delete")
        }

        Box(Modifier.weight(1f)) {
            when (content) {
                is Uri -> AsyncImage(
                    model = content,
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .width(200.dp),
                    contentScale = ContentScale.Crop
                )

                is String -> {
                    var text by remember { mutableStateOf(content) }

                    TextField(
                        value = text, onValueChange = {
                            text = it
                        })
                }
            }
        }

        Icon(imageVector = Icons.Default.Menu, contentDescription = "move position")
    }
}