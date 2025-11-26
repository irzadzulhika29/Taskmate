package com.example.taskmate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.taskmate.navigation.Screen
import com.example.taskmate.ui.components.AppBottomNavigation
import com.example.taskmate.ui.components.LogoutDialog
import com.example.taskmate.ui.components.ProfileMenuItem
import com.example.taskmate.ui.components.ProfileTopBar
import com.example.taskmate.ui.theme.BackgroundGray
import com.example.taskmate.ui.theme.DarkGray

@Composable
fun AccountScreen(navController: NavHostController, onLogout: () -> Unit) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundGray,
        bottomBar = { AppBottomNavigation(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val headerBrush = Brush.linearGradient(
                colors = listOf(Color(0xFF7C83FD), Color(0xFFA29BFE))
            )

            Column(modifier = Modifier.fillMaxSize()) {
                ProfileTopBar(title = "Akun", brush = headerBrush)

                Spacer(modifier = Modifier.height(70.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    AccountProfileCard()

                    Spacer(modifier = Modifier.height(24.dp))

                    ProfileMenuItem(
                        icon = Icons.Outlined.Edit,
                        title = "Edit Profil",
                        onClick = { navController.navigate(Screen.EditProfile.route) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileMenuItem(
                        icon = Icons.Outlined.Lock,
                        title = "Ubah Password",
                        onClick = { navController.navigate(Screen.ChangePassword.route) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileMenuItem(
                        icon = Icons.Filled.ExitToApp,
                        title = "Keluar",
                        titleColor = Color(0xFFE53935),
                        backgroundColor = Color(0xFFFFEEF0),
                        iconTint = Color(0xFFE53935),
                        onClick = { showLogoutDialog = true }
                    )
                }
            }

            if (showLogoutDialog) {
                LogoutDialog(
                    onDismiss = { showLogoutDialog = false },
                    onConfirm = {
                        showLogoutDialog = false
                        onLogout()
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountProfileCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8E4FA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Foto profil",
                    tint = Color(0xFF7C83FD),
                    modifier = Modifier.size(52.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Biru Laut",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
                Text(
                    text = "birulaut@gmail.com",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}
