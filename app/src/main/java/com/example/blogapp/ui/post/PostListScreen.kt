package com.example.blogapp.ui.post

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PostListScreen(
    onPostClick: (Int) -> Unit,
    onAddPostClick: () -> Unit
) {
    Text("Post List Screen")
}

@Preview(showBackground = true)
@Composable
fun PostListScreenPreview() {
    PostListScreen(
        onPostClick = {},
        onAddPostClick = {}
    )
}