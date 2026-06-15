package com.example.blogapp.ui.post

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PostAddScreen(
    onPostAdded: () -> Unit
) {
    Text("Post Add Screen")
}

@Preview(showBackground = true)
@Composable
fun PostAddScreenPreview() {
    PostAddScreen(
        onPostAdded = {}
    )
}