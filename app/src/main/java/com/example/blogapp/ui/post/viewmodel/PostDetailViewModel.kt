package com.example.blogapp.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PostDetailUiState {
    object Loading : PostDetailUiState()
    data class Success(val post: Post) : PostDetailUiState()
    data class Error(val message: String) : PostDetailUiState()
}

class PostDetailViewModel(
    private val repository: BlogRepository,
    private val postId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    init {
        loadPost()
    }

    private fun loadPost() {
        viewModelScope.launch {
            try {
                val post = repository.getPostById(postId)
                if (post != null) {
                    _uiState.value = PostDetailUiState.Success(post)
                } else {
                    _uiState.value = PostDetailUiState.Error("Post not found")
                }
            } catch (e: Exception) {
                _uiState.value = PostDetailUiState.Error("Failed to load post: ${e.message}")
            }
        }
    }

    fun deletePost(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = repository.deletePost(postId)
            if (result.isSuccess) {
                onSuccess()
            } else {
                _uiState.value = PostDetailUiState.Error("Failed to delete post: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}