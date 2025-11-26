package com.example.taskmate.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.taskmate.viewmodel.TaskFormState
import com.example.taskmate.viewmodel.TaskPriority
import com.example.taskmate.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AddTaskScreen(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    taskId: String?,
    mode: String
) {
    val isReadOnly = mode == "detail"

    var formState by rememberSaveable(stateSaver = TaskFormSaver()) {
        mutableStateOf(TaskFormState())
    }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            taskViewModel.getTask(taskId)?.let { task ->
                formState = TaskFormState(
                    title = task.title,
                    description = task.description,
                    category = task.category,
                    priority = task.priority,
                    date = task.date,
                    time = task.time
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Header(navController = navController, mode = mode)

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                InputField(
                    label = "Tugas",
                    value = formState.title,
                    onValueChange = { formState = formState.copy(title = it) },
                    placeholder = "Tuliskan judul tugas",
                    readOnly = isReadOnly
                )

                Spacer(modifier = Modifier.height(12.dp))

                InputField(
                    label = "Deskripsi",
                    value = formState.description,
                    onValueChange = { formState = formState.copy(description = it) },
                    placeholder = "Berikan deskripsi singkat tugas",
                    supporting = "optional",
                    readOnly = isReadOnly
                )

                Spacer(modifier = Modifier.height(12.dp))

                PriorityDropdown(
                    selected = formState.priority,
                    onSelected = { formState = formState.copy(priority = it) },
                    readOnly = isReadOnly
                )

                Spacer(modifier = Modifier.height(12.dp))

                CategoryDropdown(
                    selected = formState.category,
                    onSelected = { formState = formState.copy(category = it) },
                    readOnly = isReadOnly
                )

                Spacer(modifier = Modifier.height(12.dp))

                DateTimeRow(
                    date = formState.date,
                    time = formState.time,
                    onDateChange = { formState = formState.copy(date = it) },
                    onTimeChange = { formState = formState.copy(time = it) },
                    readOnly = isReadOnly
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        taskViewModel.upsertTask(taskId, formState, mode)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    enabled = !isReadOnly && formState.title.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C8CEB))
                ) {
                    Text(text = "Simpan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun Header(navController: NavHostController, mode: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF7C8CEB), Color(0xFF8A5CF6))
                )
            )
            .padding(horizontal = 16.dp, vertical = 20.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = if (mode == "edit") "Edit Tugas" else if (mode == "detail") "Detail Tugas" else "Tambah Tugas",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Lengkapi data tugas kamu",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean,
    supporting: String? = null
) {
    Text(text = label, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        enabled = !readOnly,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            focusedBorderColor = Color(0xFF7C8CEB)
        )
    )
    supporting?.let {
        Text(text = it, color = Color(0xFF6B7280), fontSize = 12.sp)
    }
}

@Composable
private fun PriorityDropdown(
    selected: TaskPriority,
    onSelected: (TaskPriority) -> Unit,
    readOnly: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Text(text = "Skala Prioritas", fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it && !readOnly }) {
        OutlinedTextField(
            value = selected.label,
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            enabled = !readOnly,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            TaskPriority.entries.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.label) },
                    onClick = {
                        onSelected(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryDropdown(
    selected: String,
    onSelected: (String) -> Unit,
    readOnly: Boolean
) {
    val options = listOf("Kebersihan", "Pekerjaan", "Akademik", "Kesehatan", "Rumah Tangga", "Lainnya")
    var expanded by remember { mutableStateOf(false) }

    Text(text = "Kategori", fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it && !readOnly }) {
        OutlinedTextField(
            value = if (selected.isBlank()) "Pilih kategori" else selected,
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            enabled = !readOnly,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DateTimeRow(
    date: LocalDate,
    time: LocalTime,
    onDateChange: (LocalDate) -> Unit,
    onTimeChange: (LocalTime) -> Unit,
    readOnly: Boolean
) {
    val context = LocalContext.current
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = {
                if (readOnly) return@OutlinedButton
                val today = date
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        onDateChange(LocalDate.of(year, month + 1, day))
                    },
                    today.year,
                    today.monthValue - 1,
                    today.dayOfMonth
                ).show()
            },
            modifier = Modifier.weight(1f),
            enabled = !readOnly
        ) {
            Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = "Tanggal")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = date.format(dateFormatter))
        }

        OutlinedButton(
            onClick = {
                if (readOnly) return@OutlinedButton
                val current = time
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        onTimeChange(LocalTime.of(hour, minute))
                    },
                    current.hour,
                    current.minute,
                    true
                ).show()
            },
            modifier = Modifier.weight(1f),
            enabled = !readOnly
        ) {
            Icon(imageVector = Icons.Filled.Schedule, contentDescription = "Waktu")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = time.format(timeFormatter))
        }
    }
}

@Composable
private fun TaskFormSaver() = androidx.compose.runtime.saveable.Saver<TaskFormState, Any>(
    save = {
        listOf(
            it.title,
            it.description,
            it.category,
            it.priority.name,
            it.date.toString(),
            it.time.toString()
        )
    },
    restore = {
        val values = it as List<*>
        TaskFormState(
            title = values[0] as String,
            description = values[1] as String,
            category = values[2] as String,
            priority = TaskPriority.valueOf(values[3] as String),
            date = LocalDate.parse(values[4] as String),
            time = LocalTime.parse(values[5] as String)
        )
    }
)
