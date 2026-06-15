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
import com.example.blogapp.ui.post.viewmodel.PostAddViewModel
import com.example.blogapp.ui.post.viewmodel.PostDetailViewModel
import com.example.blogapp.ui.post.viewmodel.PostListViewModel
import com.example.blogapp.ui.post.viewmodel.factory.PostAddViewModelFactory
import com.example.blogapp.ui.post.viewmodel.factory.PostDetailViewModelFactory
import com.example.blogapp.ui.post.viewmodel.factory.PostListViewModelFactory
import com.example.blogapp.util.ConnectivityObserver

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as BlogApp
    val repository = application.repository

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route,
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthenticationSuccess = {
                    navController.navigate(Screen.PostList.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Screen.PostList.route) {
            val connectivityObserver = ConnectivityObserver(context)
            val viewModel: PostListViewModel = viewModel(
                factory = PostListViewModelFactory(repository, connectivityObserver),
            )

            PostListScreen(
                onPostClick = { postId ->
                    navController.navigate(Screen.PostDetail.passId(postId))
                },
                onAddPostClick = {
                    navController.navigate(Screen.PostAdd.route)
                },
                viewModel = viewModel,
            )
        }
        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: return@composable
            val viewModel: PostDetailViewModel = viewModel(
                factory = PostDetailViewModelFactory(repository, postId),
            )

            PostDetailScreen(
                onDelete = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
            )
        }
        composable(Screen.PostAdd.route) {
            val viewModel: PostAddViewModel = viewModel(
                factory = PostAddViewModelFactory(repository),
            )

            PostAddScreen(
                onPostAdded = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
            )
        }
    }
}
