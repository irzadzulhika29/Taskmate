package com.example.taskmate.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.taskmate.navigation.Screen
import com.example.taskmate.ui.theme.DarkGray
import com.example.taskmate.ui.theme.PastelBlue
import com.example.taskmate.ui.theme.PastelGreen
import com.example.taskmate.ui.theme.PastelOrange
import com.example.taskmate.ui.theme.PastelPink
import com.example.taskmate.ui.theme.PastelPurple
import com.example.taskmate.ui.theme.TextGray
import com.example.taskmate.viewmodel.Task
import com.example.taskmate.viewmodel.TaskPriority
import com.example.taskmate.viewmodel.TaskViewModel
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavHostController, taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()
    val todayCount = taskViewModel.todayCount()
    val missedCount = taskViewModel.missedCount()
    val totalCount = taskViewModel.totalCount()

    var showDeleteDialog by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderSection(todayCount)

        SummaryCards(
            todayCount = todayCount,
            missedCount = missedCount,
            totalCount = totalCount
        )

        TaskListSection(
            tasks = tasks,
            onAddTask = {
                navController.navigate("${Screen.TaskForm.route}?mode=add")
            },
            onEditTask = { taskId ->
                navController.navigate("${Screen.TaskForm.route}?taskId=$taskId&mode=edit")
            },
            onDetailTask = { taskId ->
                navController.navigate("${Screen.TaskForm.route}?taskId=$taskId&mode=detail")
            },
            onDeleteTask = { taskId -> showDeleteDialog = taskId }
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(navController = navController)
    }

    showDeleteDialog?.let { taskId ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text(text = "Hapus Tugas") },
            text = { Text(text = "Apakah Anda yakin ingin menghapus tugas ini?") },
            confirmButton = {
                Button(onClick = {
                    taskViewModel.deleteTask(taskId)
                    showDeleteDialog = null
                }) {
                    Text("Ya, Hapus")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = PastelPurple)
                ) {
                    Text("Batal", color = DarkGray)
                }
            }
        )
    }
}

@Composable
private fun HeaderSection(todayCount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF7C8CEB), Color(0xFF8A5CF6))
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Halo, Biru!",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ada $todayCount tugas tersisa hari ini",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun SummaryCards(todayCount: Int, missedCount: Int, totalCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = (-32).dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            title = "Tugas Hari Ini",
            value = todayCount,
            backgroundColor = PastelPurple
        )
        SummaryCard(
            title = "Tugas Terlewat",
            value = missedCount,
            backgroundColor = PastelPink,
            trailing = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "↻", fontSize = 14.sp)
                }
            }
        )
        SummaryCard(
            title = "Semua Tugas",
            value = totalCount,
            backgroundColor = PastelBlue
        )
    }
}

@Composable
private fun RowScope.SummaryCard(
    title: String,
    value: Int,
    backgroundColor: Color,
    trailing: (@Composable () -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .height(110.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = value.toString(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
                if (trailing != null) trailing()
            }
            Text(
                text = title,
                fontSize = 14.sp,
                color = DarkGray
            )
        }
    }
}

@Composable
private fun TaskListSection(
    tasks: List<Task>,
    onAddTask: () -> Unit,
    onEditTask: (String) -> Unit,
    onDetailTask: (String) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tugas hari ini",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkGray
            )
            Button(
                onClick = onAddTask,
                colors = ButtonDefaults.buttonColors(containerColor = PastelOrange),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "+ Tambah Tugas", color = DarkGray, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onEditTask = onEditTask,
                    onDetailTask = onDetailTask,
                    onDeleteTask = onDeleteTask
                )
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
private fun TaskCard(
    task: Task,
    onEditTask: (String) -> Unit,
    onDetailTask: (String) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(task.colorHex)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkGray
                )
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "Opsi")
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Detail") },
                        onClick = {
                            expanded = false
                            onDetailTask(task.id)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            expanded = false
                            onEditTask(task.id)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Hapus") },
                        onClick = {
                            expanded = false
                            onDeleteTask(task.id)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = task.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                PriorityChip(task.priority)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.category,
                    fontSize = 13.sp,
                    color = DarkGray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.description,
                fontSize = 14.sp,
                color = TextGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PriorityChip(priority: TaskPriority) {
    val color = when (priority) {
        TaskPriority.HIGH -> PastelPink
        TaskPriority.MEDIUM -> PastelOrange
        TaskPriority.LOW -> PastelGreen
    }
    Text(
        text = priority.label,
        color = DarkGray,
        fontSize = 12.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
private fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Beranda") },
            label = { Text("Beranda") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.Account.route) },
            icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Akun") },
            label = { Text("Akun") }
        )
    }
}
