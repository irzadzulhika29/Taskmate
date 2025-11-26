package com.example.taskmate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmate.ui.screens.ChangePasswordScreen
import com.example.taskmate.ui.screens.LoginScreen
import com.example.taskmate.ui.screens.OnboardingScreen
import com.example.taskmate.ui.screens.RegisterScreen
import com.example.taskmate.ui.screens.AddTaskScreen
import com.example.taskmate.ui.screens.HomeScreen
import com.example.taskmate.ui.screens.AccountScreen
import com.example.taskmate.ui.screens.EditProfileScreen
import com.example.taskmate.viewmodel.TaskViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun TaskMateNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Onboarding.route
) {
    val taskViewModel: TaskViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    // Handle forgot password navigation
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController, taskViewModel = taskViewModel)
        }

        composable(Screen.Account.route) {
            AccountScreen(
                navController = navController,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }

        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(navController = navController)
        }

        composable(
            route = "${Screen.TaskForm.route}?taskId={taskId}&mode={mode}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = "add"
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            val mode = backStackEntry.arguments?.getString("mode") ?: "add"
            AddTaskScreen(
                navController = navController,
                taskViewModel = taskViewModel,
                taskId = taskId,
                mode = mode
            )
        }
    }
}

