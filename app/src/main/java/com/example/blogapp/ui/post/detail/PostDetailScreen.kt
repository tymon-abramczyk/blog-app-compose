package com.example.blogapp.ui.post.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blogapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    onDelete: () -> Unit,
    viewModel: PostDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.post_detail_title)) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is PostDetailUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is PostDetailUiState.Success -> {
                    val post = (uiState as PostDetailUiState.Success).post
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = post.title,
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            text = post.body,
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                            )
                        ) {
                            Text(stringResource(R.string.post_detail_delete_post))
                        }
                    }
                }
                is PostDetailUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(
                                R.string.post_detail_error,
                                (uiState as PostDetailUiState.Error).message
                            ),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { onDelete() }) {
                            Text(stringResource(R.string.post_detail_go_back))
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.post_detail_delete_dialog_title)) },
            text = { Text(stringResource(R.string.post_detail_delete_dialog_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deletePost(
                            onSuccess = onDelete,
                            onFailure = { errorMsg ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(context.getString(R.string.post_detail_delete_failed, errorMsg))
                                }
                            }
                        )
                    },
                ) {
                    Text(
                        text = stringResource(R.string.post_detail_delete_dialog_confirm),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.post_detail_delete_dialog_cancel))
                }
            },
        )
    }
}
