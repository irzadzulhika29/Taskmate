package com.example.taskmate.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Register : Screen("register")
    object Login : Screen("login")
}

