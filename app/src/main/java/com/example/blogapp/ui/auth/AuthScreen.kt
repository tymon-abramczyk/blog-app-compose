package com.example.blogapp.ui.auth

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.blogapp.R

@Composable
fun AuthScreen(
    onAuthenticationSuccess: () -> Unit
) {
    val context = LocalContext.current
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
                    authState = AuthState.Error(context.getString(R.string.authentication_error, errString))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    authState = AuthState.Error(context.getString(R.string.authentication_failed))
                }
            },
        )
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.authentication_prompt_title))
            .setSubtitle(context.getString(R.string.authentication_prompt_subtitle))
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
                text = stringResource(R.string.app_name),
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineMedium,
            )
            if (!canAuthenticate) {
                Text(
                    text = stringResource(R.string.authentication_unavailable),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                when(authState) {
                    is AuthState.Authenticating -> {
                        CircularProgressIndicator()
                        Text(stringResource(R.string.authentication_in_progress))
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
                            Text(stringResource(R.string.authentication_retry))
                        }
                    }
                    is AuthState.Success -> {
                        Text(
                            text = stringResource(R.string.authentication_successful),
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
