package com.example.taskmate.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    taskId: String?,
    mode: String
) {
    val isReadOnly = mode == "detail"

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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(navController, mode)
            FormContent(
                formState = formState,
                onFormStateChange = { formState = it },
                isReadOnly = isReadOnly,
                onSave = {
                    taskViewModel.upsertTask(taskId, formState, mode)
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun Header(navController: NavHostController, mode: String) {
    val title = when (mode) {
        "add" -> "Tambah Tugas"
        "edit" -> "Edit Tugas"
        else -> "Detail Tugas"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF7C8CEB), Color(0xFF8A5CF6))
                )
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun FormContent(
    formState: TaskFormState,
    onFormStateChange: (TaskFormState) -> Unit,
    isReadOnly: Boolean,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputField(
                label = "Judul Tugas",
                value = formState.title,
                onValueChange = { onFormStateChange(formState.copy(title = it)) },
                placeholder = "Masukkan judul tugas",
                readOnly = isReadOnly
            )

            InputField(
                label = "Deskripsi",
                value = formState.description,
                onValueChange = { onFormStateChange(formState.copy(description = it)) },
                placeholder = "Masukkan deskripsi tugas",
                readOnly = isReadOnly,
                minLines = 3
            )

            PriorityDropdown(
                selected = formState.priority,
                onSelected = { onFormStateChange(formState.copy(priority = it)) },
                readOnly = isReadOnly
            )

            CategoryDropdown(
                selected = formState.category,
                onSelected = { onFormStateChange(formState.copy(category = it)) },
                readOnly = isReadOnly
            )

            DateTimeRow(
                dateMillis = formState.dateMillis,
                timeHour = formState.timeHour,
                timeMinute = formState.timeMinute,
                onDateChange = { onFormStateChange(formState.copy(dateMillis = it)) },
                onTimeChange = { hour, minute ->
                    onFormStateChange(formState.copy(timeHour = hour, timeMinute = minute))
                },
                readOnly = isReadOnly
            )

            if (!isReadOnly) {
                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C8CEB)),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isReadOnly && formState.title.isNotBlank(),
                ) {
                    Text(text = "Simpan", fontSize = 16.sp, color = Color.White)
                }
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
    minLines: Int = 1
) {
    Text(text = label, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        enabled = !readOnly,
        minLines = minLines,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7C8CEB)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
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

@OptIn(ExperimentalMaterial3Api::class)
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
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
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
            enabled = !readOnly
        ) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Tanggal")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = dateFormatter.format(Date(dateMillis)))
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
            enabled = !readOnly
        ) {
            Text(text = "🕐")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = String.format(Locale.getDefault(), "%02d:%02d", timeHour, timeMinute))
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

