package com.example.blogapp.ui.auth

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun AuthScreen(
    onAuthenticationSuccess: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = ContextCompat.getMainExecutor(context)

    var authState by remember { mutableStateOf<AuthState>(AuthState.Idle) }

    val biometricManager = BiometricManager.from(context)
    val authenticators =
        BiometricManager.Authenticators.BIOMETRIC_STRONG or
        BiometricManager.Authenticators.DEVICE_CREDENTIAL
    val canAuthenticate = when (biometricManager.canAuthenticate(authenticators)) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> false
    }

    val biometricPrompt = remember {
        BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    authState = AuthState.Success
                    onAuthenticationSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    authState = AuthState.Error("Authentication error: $errString")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    authState = AuthState.Error("Authentication failed. Please try again.")
                }
            },
        )
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Log in to access your blog posts")
            .setDescription("Use fingerprint, face or PIN to continue")
            .setAllowedAuthenticators(authenticators)
            .build()
    }

    LaunchedEffect(Unit) {
        if (canAuthenticate && authState == AuthState.Idle) {
            authState = AuthState.Authenticating
            biometricPrompt.authenticate(promptInfo)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Blog App",
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineMedium,
            )
            if (!canAuthenticate) {
                Text(
                    text = "Biometric authentication is not available on this device.\nPlease set up fingerprint/PIN in system settings.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                when(authState) {
                    is AuthState.Authenticating -> {
                        CircularProgressIndicator()
                        Text(text = "Authenticating...")
                    }
                    is AuthState.Error -> {
                        val errorString = (authState as AuthState.Error).error
                        Text(
                            text = errorString,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Button(onClick = {
                            authState = AuthState.Authenticating
                            biometricPrompt.authenticate(promptInfo)
                        }) {
                            Text("Retry")
                        }
                    }
                    is AuthState.Success -> {
                        Text(
                            text = "Authentication successful!",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(onAuthenticationSuccess = {})
}
