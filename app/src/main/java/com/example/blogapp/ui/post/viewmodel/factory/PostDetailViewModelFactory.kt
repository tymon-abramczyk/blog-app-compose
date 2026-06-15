package com.example.blogapp.ui.post.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blogapp.data.repository.BlogRepository
import com.example.blogapp.ui.post.viewmodel.PostDetailViewModel

class PostDetailViewModelFactory(
    private val repository: BlogRepository,
    private val postId: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostDetailViewModel(repository, postId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}