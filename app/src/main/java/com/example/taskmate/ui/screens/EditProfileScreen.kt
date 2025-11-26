package com.example.taskmate.ui.screens

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.taskmate.navigation.Screen
import com.example.taskmate.ui.theme.BackgroundGray
import com.example.taskmate.ui.theme.DarkGray

@Composable
fun EditProfileScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("Biru Laut") }
    var email by remember { mutableStateOf("alfitsani@gmail.com") }
    var phone by remember { mutableStateOf("+62 812 3456 7890") }
    var birthDate by remember { mutableStateOf("20/07/2002") }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val datePickerDialog = remember {
        DatePickerDialog(context).apply {
            setOnDateSetListener { _, year, month, dayOfMonth ->
                val monthValue = month + 1
                birthDate = "%02d/%02d/%04d".format(dayOfMonth, monthValue, year)
            }
        }
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImage = uri }
    )

    Scaffold(containerColor = BackgroundGray) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val brush = Brush.linearGradient(listOf(Color(0xFF7C83FD), Color(0xFF9B8CFA)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(brush)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                    Text(text = "Edit Profil", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-50).dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8E4FA)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Foto profil",
                                tint = Color(0xFF7C83FD),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                        IconButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Ganti foto",
                                tint = Color(0xFF7C83FD)
                            )
                        }
                    }

                    ProfileTextField(label = "Nama", value = name, onValueChange = { name = it })
                    ProfileTextField(label = "Email", value = email, onValueChange = { email = it })
                    ProfileTextField(label = "Nomor Telepon", value = phone, onValueChange = { phone = it })
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { birthDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Tanggal Lahir") },
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Pilih tanggal", tint = Color(0xFF7C83FD))
                            }
                        },
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7C83FD),
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.popBackStack(Screen.Account.route, false) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C83FD))
                        ) {
                            Text(text = "Simpan", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.navigate(Screen.ChangePassword.route) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            border = ButtonDefaults.outlinedButtonBorder,
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Text(text = "Ubah Password", color = Color(0xFF7C83FD))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.SemiBold, color = DarkGray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF7C83FD),
                unfocusedBorderColor = Color(0xFFE5E7EB)
            )
        )
    }
}
