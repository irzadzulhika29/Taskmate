package com.example.taskmate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmate.ui.components.SocialLoginButton
import com.example.taskmate.ui.theme.*
import com.example.taskmate.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with gradient background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF7C3AED),
                            PrimaryBlue
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "T",
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // White panel with rounded top corners
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-40).dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.White)
                .padding(24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = "Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email Input
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("birulaut@gmail.com") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email"
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BorderGray,
                    focusedBorderColor = PrimaryBlue
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input with Forgot Password
            Column {
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("••••••••") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Password"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                            Text(
                                text = if (uiState.isPasswordVisible) "👁" else "👁‍🗨",
                                fontSize = 20.sp
                            )
                        }
                    },
                    visualTransformation = if (uiState.isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderGray,
                        focusedBorderColor = PrimaryBlue
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Forgot Password Link
                Text(
                    text = "Lupa Password?",
                    color = PrimaryBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onNavigateToForgotPassword() }
                )
            }

            // Error message
            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { viewModel.login(onLoginSuccess) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderGray)
                Text(
                    text = "atau masuk dengan",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = TextGray,
                    fontSize = 14.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Login Button
            SocialLoginButton(
                text = "Google",
                onClick = { viewModel.loginWithGoogle(onLoginSuccess) },
                icon = "G" // In real app, use proper Google icon
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Apple Login Button
            SocialLoginButton(
                text = "Apple ID",
                onClick = { viewModel.loginWithApple(onLoginSuccess) },
                icon = "" // In real app, use proper Apple icon
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Footer - Register Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = TextGray)) {
                            append("Belum memiliki akun? ")
                        }
                        withStyle(style = SpanStyle(color = PrimaryBlue, fontWeight = FontWeight.SemiBold)) {
                            append("Register")
                        }
                    },
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

