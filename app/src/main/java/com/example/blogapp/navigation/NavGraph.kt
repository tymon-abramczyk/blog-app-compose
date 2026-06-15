package com.example.blogapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.blogapp.BlogApp
import com.example.blogapp.ui.auth.AuthScreen
import com.example.blogapp.ui.post.PostAddScreen
import com.example.blogapp.ui.post.PostDetailScreen
import com.example.blogapp.ui.post.PostListScreen
import com.example.blogapp.ui.post.viewmodel.PostDetailViewModel
import com.example.blogapp.ui.post.viewmodel.PostListViewModel
import com.example.blogapp.ui.post.viewmodel.factory.PostDetailViewModelFactory
import com.example.blogapp.ui.post.viewmodel.factory.PostListViewModelFactory

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as BlogApp
    val repository = application.repository

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthenticationSuccess = {
                    navController.navigate(Screen.PostList.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.PostList.route) {
            val viewModel: PostListViewModel = viewModel(
                factory = PostListViewModelFactory(repository)
            )

            PostListScreen(
                onPostClick = { postId ->
                    navController.navigate(Screen.PostDetail.passId(postId))
                },
                onAddPostClick = {
                    navController.navigate(Screen.PostAdd.route)
                },
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: return@composable
            val viewModel: PostDetailViewModel = viewModel(
                factory = PostDetailViewModelFactory(repository, postId)
            )

            PostDetailScreen(
                onDelete = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
        composable(Screen.PostAdd.route) {
            PostAddScreen(
                onPostAdded = {
                    navController.popBackStack()
                }
            )
        }
    }
}
