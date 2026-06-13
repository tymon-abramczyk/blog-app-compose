package com.example.blogapp.ui.auth

sealed class AuthState {
    object Idle : AuthState()
    object Authenticating : AuthState()
    object Success : AuthState()
    data class Error(val error: String) : AuthState()
}