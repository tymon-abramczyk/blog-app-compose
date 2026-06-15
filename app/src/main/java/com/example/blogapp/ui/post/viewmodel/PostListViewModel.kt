package com.example.blogapp.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

sealed class PostListUiState {
    object Loading : PostListUiState()
    data class Success(val posts: List<Post>) : PostListUiState()
    data class Error(val message: String) : PostListUiState()
}

class PostListViewModel(
    private val repository: BlogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostListUiState>(PostListUiState.Loading)
    val uiState: StateFlow<PostListUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadPosts()
        viewModelScope.launch {
            if (repository.getAllPosts().firstOrNull().isNullOrEmpty()) {
                refreshPosts()
            }
        }
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = PostListUiState.Loading
            repository.getAllPosts()
                .catch { e ->
                    _uiState.value = PostListUiState.Error(e.message ?: "Unknown error")
                }
                .collect { posts ->
                    _uiState.value = PostListUiState.Success(posts)
                }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.refreshPosts()
            } catch (e: Exception) {
                _uiState.value = PostListUiState.Error(e.message ?: "Failed to refresh posts")
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}