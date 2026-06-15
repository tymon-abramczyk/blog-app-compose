package com.example.blogapp.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.repository.BlogRepository
import com.example.blogapp.util.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class PostListUiState {
    object Loading : PostListUiState()
    data class Success(val posts: List<Post>) : PostListUiState()
    data class Error(val message: String) : PostListUiState()
}

class PostListViewModel(
    private val repository: BlogRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostListUiState>(PostListUiState.Loading)
    val uiState: StateFlow<PostListUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    init {
        loadPosts()
        observeConnectivity()
        viewModelScope.launch {
            refreshPosts()
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

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { isConnected ->
                _isOffline.value = !isConnected
            }
        }
    }
}