package com.example.taskmate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.taskmate.navigation.Screen
import com.example.taskmate.ui.theme.DarkGray
import com.example.taskmate.ui.theme.PastelPurple
import com.example.taskmate.viewmodel.Task
import com.example.taskmate.viewmodel.TaskPriority
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali")
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    )
}

@Composable
fun SummaryTile(title: String, value: Int, modifier: Modifier = Modifier, backgroundColor: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, color = Color(0xFF111827), fontSize = 14.sp)
            Text(
                text = value.toString(),
                color = Color(0xFF111827),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onDetailTask: (String) -> Unit,
    onEditTask: (String) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(task.colorHex)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = task.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = task.getTimeString(),
                        color = Color(0xFF4B5563),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                TaskOverflowMenu(
                    taskId = task.id,
                    onDetailTask = onDetailTask,
                    onEditTask = onEditTask,
                    onDeleteTask = onDeleteTask
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = task.description, color = Color(0xFF4B5563), fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PriorityChip(priority = task.priority)
                Text(text = task.category, color = Color(0xFF111827), fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun PriorityChip(priority: TaskPriority) {
    val color = when (priority) {
        TaskPriority.HIGH -> Color(0xFFFCE4E9)
        TaskPriority.MEDIUM -> Color(0xFFFFEEE5)
        TaskPriority.LOW -> Color(0xFFE8F6ED)
    }

    Surface(
        color = color,
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = priority.label,
            color = Color(0xFF111827),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun TaskOverflowMenu(
    taskId: String,
    onDetailTask: (String) -> Unit,
    onEditTask: (String) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opsi")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Detail") },
                onClick = {
                    expanded = false
                    onDetailTask(taskId)
                }
            )
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    expanded = false
                    onEditTask(taskId)
                }
            )
            DropdownMenuItem(
                text = { Text("Hapus") },
                onClick = {
                    expanded = false
                    onDeleteTask(taskId)
                }
            )
        }
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    minLines: Int = 1,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            enabled = !readOnly,
            readOnly = readOnly,
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) { content() }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    launchSingleTop = true
                    popUpTo(Screen.Home.route) { inclusive = false }
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Beranda") },
            label = { Text("Beranda") }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Account.route,
            onClick = {
                navController.navigate(Screen.Account.route) {
                    launchSingleTop = true
                    popUpTo(Screen.Home.route) { inclusive = false }
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Akun") },
            label = { Text("Akun") }
        )
    }
}

@Composable
fun ProfileTopBar(title: String, brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .background(brush),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    backgroundColor: Color = Color.White,
    titleColor: Color = DarkGray,
    iconTint: Color = DarkGray,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .padding(horizontal = 0.dp)
            .background(Color.Transparent),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PastelPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint
                    )
                }
                Text(text = title, color = titleColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF9CA3AF)
            )
        }
    }
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEEF0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = Color(0xFFE53935)
                    )
                }
                Text(
                    text = "Apakah Anda yakin untuk Keluar?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DarkGray,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder,
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(text = "Kembali", color = Color(0xFF7C83FD))
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C83FD))
                    ) {
                        Text(text = "Keluar", color = Color.White)
                    }
                }
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(18.dp),
        tonalElevation = 0.dp
    )
}
