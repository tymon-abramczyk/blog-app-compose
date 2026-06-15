package com.example.blogapp.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class PostAddUiState {
    object Idle : PostAddUiState()
    object Loading : PostAddUiState()
    data class Success(val post: Post) : PostAddUiState()
    data class Error(val message: String) : PostAddUiState()
}

class PostAddViewModel(
    private val repository: BlogRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostAddUiState>(PostAddUiState.Loading)
    val uiState: StateFlow<PostAddUiState> = _uiState.asStateFlow()

    fun addPost(title: String, body: String, onSuccess: () -> Unit) {
        if (title.isBlank() || body.isBlank()) {
            _uiState.value = PostAddUiState.Error("Title and body cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = PostAddUiState.Loading
            val result = repository.createPost(title, body)
            if (result.isSuccess) {
                _uiState.value = PostAddUiState.Success(result.getOrNull()!!)
                onSuccess()
            } else {
                val errorMsg = when (val e = result.exceptionOrNull()) {
                    is UnknownHostException -> "No internet connection"
                    is SocketTimeoutException -> "Network timeout"
                    else -> e?.message ?: "Failed to add post"
                }
                _uiState.value = PostAddUiState.Error(errorMsg)
            }
        }
    }

    fun resetState() {
        _uiState.value = PostAddUiState.Idle
    }
}