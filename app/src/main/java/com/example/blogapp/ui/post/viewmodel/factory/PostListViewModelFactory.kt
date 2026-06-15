package com.example.blogapp.ui.post.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blogapp.data.repository.BlogRepository
import com.example.blogapp.ui.post.viewmodel.PostListViewModel
import com.example.blogapp.util.ConnectivityObserver

class PostListViewModelFactory(
    private val repository: BlogRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostListViewModel(repository, connectivityObserver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}