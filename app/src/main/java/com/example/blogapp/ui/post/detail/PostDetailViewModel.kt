package com.example.blogapp.ui.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.R
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class PostDetailUiState {
    object Loading : PostDetailUiState()
    data class Success(val post: Post) : PostDetailUiState()
    data class Error(val messageRes: Int, val message: String = "") : PostDetailUiState()
}

class PostDetailViewModel(
    private val repository: BlogRepository,
    private val postId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private val cachedPost = MutableStateFlow<Post?>(null)

    init {
        loadPost()
    }

    private fun loadPost() {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading

            try {
                val post = repository.getPostById(postId)
                if (post != null) {
                    _uiState.value = PostDetailUiState.Success(post)
                    cachedPost.value = post
                } else {
                    _uiState.value = PostDetailUiState.Error(R.string.post_detail_error_not_found)
                }
            } catch (e: Exception) {
                _uiState.value = PostDetailUiState.Error(R.string.post_detail_load_failed, e.message ?: "Unknown error")
            }
        }
    }

    fun deletePost(
        onSuccess: () -> Unit,
        onFailure: (Int) -> Unit = {},
    ) {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading

            val result = repository.deletePost(postId)
            if (result.isSuccess) {
                onSuccess()
            } else {
                when (val e = result.exceptionOrNull()) {
                    is UnknownHostException -> {
                        _uiState.value = PostDetailUiState.Success(cachedPost.value ?: return@launch)
                        onFailure(R.string.error_no_internet)
                    }
                    is SocketTimeoutException -> {
                        _uiState.value = PostDetailUiState.Success(cachedPost.value ?: return@launch)
                        onFailure(R.string.error_network_timeout)
                    }
                    else -> {
                        _uiState.value = PostDetailUiState.Error(R.string.post_detail_delete_error, e?.message ?: "Unknown error")
                    }
                }
            }
        }
    }
}