package com.example.blogapp.ui.post

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blogapp.ui.post.viewmodel.PostAddUiState
import com.example.blogapp.ui.post.viewmodel.PostAddViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAddScreen(
    onPostAdded: () -> Unit,
    viewModel: PostAddViewModel,
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(uiState) {
        if (uiState is PostAddUiState.Success) {
            onPostAdded()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Post") }
            )
        },
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                isError = uiState is PostAddUiState.Error && title.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("Body") },
                isError = uiState is PostAddUiState.Error && body.isBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            if (uiState is PostAddUiState.Error) {
                Text(
                    text = (uiState as PostAddUiState.Error).message,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Button(
                onClick = { viewModel.addPost(title, body, onPostAdded) },
                enabled = uiState !is PostAddUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState is PostAddUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Submit")
                }
            }
        }
    }
}
