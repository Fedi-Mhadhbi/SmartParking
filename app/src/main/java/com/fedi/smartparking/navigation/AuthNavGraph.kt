package com.fedi.smartparking.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fedi.smartparking.features.auth.AuthViewModel
import com.fedi.smartparking.features.auth.ForgotPasswordScreen
import com.fedi.smartparking.features.auth.LoginScreen
import com.fedi.smartparking.features.auth.RegisterScreen
import com.fedi.smartparking.features.auth.SplashScreen
import com.fedi.smartparking.features.auth.WelcomeScreen

object AuthRoutes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot-password"
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onAuthenticated: () -> Unit,
) {
    composable(AuthRoutes.SPLASH) {
        SplashScreen(
            onFinished = {
                navController.navigate(AuthRoutes.WELCOME) {
                    popUpTo(AuthRoutes.SPLASH) { inclusive = true }
                }
            },
        )
    }
    composable(AuthRoutes.WELCOME) {
        WelcomeScreen(
            onGetStarted = { navController.navigate(AuthRoutes.REGISTER) },
            onSignIn = { navController.navigate(AuthRoutes.LOGIN) },
        )
    }
    composable(AuthRoutes.LOGIN) {
        AuthLoginDestination(
            navController = navController,
            onAuthenticated = onAuthenticated,
        )
    }
    composable(AuthRoutes.REGISTER) {
        AuthRegisterDestination(
            navController = navController,
            onAuthenticated = onAuthenticated,
        )
    }
    composable(AuthRoutes.FORGOT_PASSWORD) {
        ForgotPasswordScreen(
            onSendLink = {},
            onBackToLogin = { navController.popBackStack() },
        )
    }
}

@Composable
private fun AuthLoginDestination(
    navController: NavHostController,
    onAuthenticated: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
) {
    LoginScreen(
        onLogin = { email, password ->
            authViewModel.login(email, password)
            onAuthenticated()
        },
        onRegister = { navController.navigate(AuthRoutes.REGISTER) },
        onForgotPassword = { navController.navigate(AuthRoutes.FORGOT_PASSWORD) },
    )
}

@Composable
private fun AuthRegisterDestination(
    navController: NavHostController,
    onAuthenticated: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
) {
    RegisterScreen(
        onRegister = { fullName, email, password ->
            authViewModel.register(fullName, email, password)
            onAuthenticated()
        },
        onLogin = { navController.navigate(AuthRoutes.LOGIN) },
    )
}
