package com.example.blogapp.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object PostList : Screen("post_list")
    object PostDetail : Screen("post_detail/{postId}") {
        fun passId(postId: Int) = "post_detail/$postId"
    }
    object PostAdd : Screen("post_add")
}