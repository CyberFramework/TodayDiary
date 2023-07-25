package com.mh.todaydiary.ui.adddiary

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    val mContext = LocalContext.current
    val activityResultContracts = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            viewModel.addContent(it.toString())
            if (Build.VERSION.SDK_INT >= 30) {
                mContext.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                activityResultContracts.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                Toast.makeText(mContext, "권한 요청을 거부하면 사진을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.weight(1f)) {
            itemsIndexed(uiState.context) { index, content ->
                ContentContainer(
                    content = if (content.startsWith("content://")) content.toUri() else content,
                    onDelete = { viewModel.removeContent(index) },
                    onChange = { viewModel.updateContent(index, it) }
                )
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
                if (Build.VERSION.SDK_INT >= 30) {
                    activityResultContracts.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
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
private fun ContentContainer(content: Any, onDelete: () -> Unit, onChange: (String) -> Unit) {
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
                    TextField(
                        value = content, onValueChange = {
                            onChange(it)
                        })
                }
            }
        }

        Icon(imageVector = Icons.Default.Menu, contentDescription = "move position")
    }
}