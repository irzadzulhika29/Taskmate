package com.example.taskmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.taskmate.navigation.Screen
import com.example.taskmate.ui.components.AppBottomNavigation
import com.example.taskmate.ui.components.SummaryTile
import com.example.taskmate.ui.components.TaskAppBar
import com.example.taskmate.ui.components.TaskCard
import com.example.taskmate.ui.theme.DarkGray
import com.example.taskmate.ui.theme.PastelBlue
import com.example.taskmate.ui.theme.PastelPink
import com.example.taskmate.ui.theme.PastelPurple
import com.example.taskmate.viewmodel.Task
import com.example.taskmate.viewmodel.TaskViewModel

@Composable
fun HomeScreen(navController: NavHostController, taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()
    val todayCount = taskViewModel.todayCount()
    val missedCount = taskViewModel.missedCount()
    val totalCount = taskViewModel.totalCount()

    var showDeleteDialog by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TaskAppBar(
                title = "Dashboard",
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Account.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Menu akun"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("${Screen.TaskForm.route}?mode=add") },
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Tambah tugas") },
                text = { Text("Tambah Tugas") }
            )
        },
        bottomBar = { AppBottomNavigation(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            SummaryRow(
                todayCount = todayCount,
                missedCount = missedCount,
                totalCount = totalCount
            )

            Spacer(modifier = Modifier.height(16.dp))

            TaskList(
                tasks = tasks,
                onDetailTask = { navController.navigate("${Screen.TaskForm.route}?taskId=$it&mode=detail") },
                onEditTask = { navController.navigate("${Screen.TaskForm.route}?taskId=$it&mode=edit") },
                onDeleteTask = { taskId -> showDeleteDialog = taskId }
            )
        }
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
private fun SummaryRow(todayCount: Int, missedCount: Int, totalCount: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Ringkasan",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGray
        )
        RowWithSpacing {
            SummaryTile(
                title = "Hari Ini",
                value = todayCount,
                backgroundColor = PastelPurple,
                modifier = Modifier.weight(1f)
            )
            SummaryTile(
                title = "Terlewat",
                value = missedCount,
                backgroundColor = PastelPink,
                modifier = Modifier.weight(1f)
            )
            SummaryTile(
                title = "Total",
                value = totalCount,
                backgroundColor = PastelBlue,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TaskList(
    tasks: List<Task>,
    onDetailTask: (String) -> Unit,
    onEditTask: (String) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tugas",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGray
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (tasks.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onDetailTask = onDetailTask,
                        onEditTask = onEditTask,
                        onDeleteTask = onDeleteTask
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Belum ada tugas", color = DarkGray, fontWeight = FontWeight.Medium)
        Text(text = "Tekan tombol tambah untuk mulai", color = DarkGray)
    }
}

@Composable
private fun RowWithSpacing(content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        content()
    }
}

