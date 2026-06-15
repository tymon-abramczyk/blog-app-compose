package com.example.blogapp.ui.post

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PostDetailScreen(
    postId: Int,
    onDelete: () -> Unit,
) {
    Text("Post Detail Screen")
}

@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview() {
    PostDetailScreen(
        postId = 1,
        onDelete = {}
    )
}