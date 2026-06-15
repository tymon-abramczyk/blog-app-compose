package com.example.blogapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.blogapp.navigation.NavGraph
import com.example.blogapp.ui.auth.AuthScreen
import com.example.blogapp.ui.theme.BlogAppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlogAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainComposable(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainComposable(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        NavGraph()
    }
}

@Preview(showBackground = true)
@Composable
fun MainComposablePreview() {
    BlogAppTheme {
        MainComposable()
    }
}