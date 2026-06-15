package com.example.blogapp.data.repository

import com.example.blogapp.data.model.Post
import com.example.blogapp.data.model.dao.PostDao
import com.example.blogapp.data.remote.ApiService
import kotlinx.coroutines.flow.Flow

class BlogRepository(
    private val postDao: PostDao,
    private val apiService: ApiService,
) {

    fun getAllPosts(): Flow<List<Post>> = postDao.getAllPosts()

    suspend fun getPostById(postId: Int): Post? = postDao.getPostById(postId)

    suspend fun refreshPosts() {
        val posts = apiService.getPosts()
        postDao.insertAll(posts)
    }

    suspend fun deletePost(postId: Int): Result<Unit> {
        return try {
            apiService.deletePost(postId)
            postDao.deletePostById(postId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPost(title: String, body: String): Result<Post> {
        val newPost = Post(
            id = 0,
            title = title,
            body = body,
        )
        return try {
            val createdPost = apiService.createPost(newPost)
            postDao.insert(createdPost)
            Result.success(createdPost)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}