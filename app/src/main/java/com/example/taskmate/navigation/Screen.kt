package com.example.taskmate.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Register : Screen("register")
    object Login : Screen("login")
    object Home : Screen("home")
    object TaskForm : Screen("taskForm")
    object Account : Screen("account")
    object EditProfile : Screen("editProfile")
    object ChangePassword : Screen("changePassword")
}

