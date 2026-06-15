package com.example.blogapp

import android.app.Application
import com.example.blogapp.data.database.PostDatabase
import com.example.blogapp.data.remote.RetrofitInstance
import com.example.blogapp.data.repository.BlogRepository

class BlogApp : Application() {
    lateinit var database: PostDatabase
        private set

    lateinit var repository: BlogRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = PostDatabase.getInstance(this)
        repository = BlogRepository(
            postDao = database.postDao(),
            apiService = RetrofitInstance.api,
        )
    }
}