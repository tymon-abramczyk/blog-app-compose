package com.example.blogapp.ui.post.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blogapp.data.repository.BlogRepository

class PostAddViewModelFactory(
    private val repository: BlogRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostAddViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostAddViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}