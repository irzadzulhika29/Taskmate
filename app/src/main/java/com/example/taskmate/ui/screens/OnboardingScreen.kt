package com.example.taskmate.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.theme.PrimaryBlue
import com.example.taskmate.ui.theme.TextDark
import com.example.taskmate.ui.theme.TextGray
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(
    onNavigateToRegister: () -> Unit
) {
    // Auto navigate after 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        onNavigateToRegister()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Welcome Illustration
        // Note: Add your welcome illustration drawable to res/drawable
        // For now, using a placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Add actual illustration
            // Image(
            //     painter = painterResource(id = R.drawable.ic_welcome),
            //     contentDescription = "Welcome Illustration",
            //     modifier = Modifier.fillMaxSize()
            // )
            Text(
                text = "🎉\nWELCOME!",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                fontSize = 48.sp
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Heading with styled TaskMate text
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = TextDark)) {
                    append("Selamat Datang di ")
                }
                withStyle(style = SpanStyle(color = PrimaryBlue)) {
                    append("TaskMate!")
                }
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = "TaskMate siap menjadi rekan andalan Anda dalam menuntaskan setiap tugas dengan mudah dan terorganisir.",
            fontSize = 16.sp,
            color = TextGray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

