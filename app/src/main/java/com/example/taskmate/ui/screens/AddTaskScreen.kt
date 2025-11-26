package com.example.taskmate.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.taskmate.ui.components.LabeledTextField
import com.example.taskmate.ui.components.SectionCard
import com.example.taskmate.ui.components.TaskAppBar
import com.example.taskmate.viewmodel.TaskFormState
import com.example.taskmate.viewmodel.TaskPriority
import com.example.taskmate.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    taskId: String?,
    mode: String
) {
    val isReadOnly = mode == "detail"
    val title = when (mode) {
        "edit" -> "Edit Tugas"
        "detail" -> "Detail Tugas"
        else -> "Tambah Tugas"
    }

    var formState by rememberSaveable(stateSaver = taskFormSaver()) {
        mutableStateOf(TaskFormState())
    }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            val task = taskViewModel.getTask(taskId)
            if (task != null) {
                formState = TaskFormState(
                    title = task.title,
                    description = task.description,
                    category = task.category,
                    priority = task.priority,
                    dateMillis = task.dateMillis,
                    timeHour = task.timeHour,
                    timeMinute = task.timeMinute
                )
            }
        }
    }

    Scaffold(
        topBar = { TaskAppBar(title = title, onBack = { navController.popBackStack() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    LabeledTextField(
                        label = "Judul Tugas",
                        value = formState.title,
                        placeholder = "Masukkan judul tugas",
                        onValueChange = { formState = formState.copy(title = it) },
                        readOnly = isReadOnly
                    )

                    LabeledTextField(
                        label = "Deskripsi",
                        value = formState.description,
                        placeholder = "Masukkan deskripsi tugas",
                        onValueChange = { formState = formState.copy(description = it) },
                        readOnly = isReadOnly,
                        minLines = 3
                    )

                    PriorityDropdown(
                        selected = formState.priority,
                        onSelected = { formState = formState.copy(priority = it) },
                        readOnly = isReadOnly
                    )

                    CategoryDropdown(
                        selected = formState.category,
                        onSelected = { formState = formState.copy(category = it) },
                        readOnly = isReadOnly
                    )

                    DateTimeRow(
                        dateMillis = formState.dateMillis,
                        timeHour = formState.timeHour,
                        timeMinute = formState.timeMinute,
                        onDateChange = { formState = formState.copy(dateMillis = it) },
                        onTimeChange = { hour, minute ->
                            formState = formState.copy(timeHour = hour, timeMinute = minute)
                        },
                        readOnly = isReadOnly
                    )

                    if (!isReadOnly) {
                        Button(
                            onClick = {
                                taskViewModel.upsertTask(taskId, formState, mode)
                                navController.popBackStack()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(),
                            shape = RoundedCornerShape(12.dp),
                            enabled = formState.title.isNotBlank()
                        ) {
                            Text(text = "Simpan", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriorityDropdown(
    selected: TaskPriority,
    onSelected: (TaskPriority) -> Unit,
    readOnly: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = "Skala Prioritas", fontWeight = FontWeight.SemiBold)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it && !readOnly }) {
            OutlinedTextField(
                value = selected.label,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                enabled = !readOnly,
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            androidx.compose.material3.DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                TaskPriority.entries.forEach { priority ->
                    androidx.compose.material3.DropdownMenuItem(
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    selected: String,
    onSelected: (String) -> Unit,
    readOnly: Boolean
) {
    val options = listOf("Kebersihan", "Pekerjaan", "Akademik", "Kesehatan", "Rumah Tangga", "Lainnya")
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = "Kategori", fontWeight = FontWeight.SemiBold)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it && !readOnly }) {
            OutlinedTextField(
                value = if (selected.isBlank()) "Pilih kategori" else selected,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                enabled = !readOnly,
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            androidx.compose.material3.DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    androidx.compose.material3.DropdownMenuItem(
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
}

@Composable
private fun DateTimeRow(
    dateMillis: Long,
    timeHour: Int,
    timeMinute: Int,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    readOnly: Boolean
) {
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = {
                if (readOnly) return@OutlinedButton
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = dateMillis
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        val newCalendar = Calendar.getInstance()
                        newCalendar.set(year, month, day)
                        onDateChange(newCalendar.timeInMillis)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.weight(1f),
            enabled = !readOnly,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Tanggal")
            Spacer(modifier = Modifier.height(0.dp))
            Text(text = dateFormatter.format(Date(dateMillis)), modifier = Modifier.padding(start = 8.dp))
        }

        OutlinedButton(
            onClick = {
                if (readOnly) return@OutlinedButton
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        onTimeChange(hour, minute)
                    },
                    timeHour,
                    timeMinute,
                    true
                ).show()
            },
            modifier = Modifier.weight(1f),
            enabled = !readOnly,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Waktu")
            Text(
                text = String.format(Locale.getDefault(), "%02d:%02d", timeHour, timeMinute),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun taskFormSaver() = androidx.compose.runtime.saveable.Saver<TaskFormState, Any>(
    save = {
        listOf(
            it.title,
            it.description,
            it.category,
            it.priority.name,
            it.dateMillis,
            it.timeHour,
            it.timeMinute
        )
    },
    restore = {
        val values = it as List<*>
        TaskFormState(
            title = values[0] as String,
            description = values[1] as String,
            category = values[2] as String,
            priority = TaskPriority.valueOf(values[3] as String),
            dateMillis = values[4] as Long,
            timeHour = values[5] as Int,
            timeMinute = values[6] as Int
        )
    }
)
