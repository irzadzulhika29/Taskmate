package com.example.taskmate.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

enum class TaskPriority(val label: String) {
    HIGH("Tinggi"),
    MEDIUM("Sedang"),
    LOW("Rendah")
}

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val category: String,
    val priority: TaskPriority,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime,
    val colorHex: Long,
    val isMissed: Boolean = false
)

data class TaskFormState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val priority: TaskPriority = TaskPriority.HIGH,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now()
)

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(
        listOf(
            Task(
                title = "Buang Sampah",
                description = "Kumpulkan sampah rumah dan buang ke TPS",
                category = "Kebersihan",
                priority = TaskPriority.HIGH,
                time = LocalTime.of(7, 0),
                colorHex = 0xFFFFF6DAL,
                isMissed = true
            ),
            Task(
                title = "Cek Email",
                description = "Balas email penting dari dosen",
                category = "Pekerjaan",
                priority = TaskPriority.MEDIUM,
                time = LocalTime.of(9, 50),
                colorHex = 0xFFE1F3F9L
            ),
            Task(
                title = "Selesaikan Metopen",
                description = "Lengkapi bab 3 metodologi",
                category = "Akademik",
                priority = TaskPriority.HIGH,
                time = LocalTime.of(15, 0),
                colorHex = 0xFFFCE4E9L,
                isMissed = true
            ),
            Task(
                title = "Olahraga Ringan",
                description = "Lari 20 menit di sekitar kompleks",
                category = "Kesehatan",
                priority = TaskPriority.LOW,
                time = LocalTime.of(17, 30),
                colorHex = 0xFFE8F6EDL
            ),
            Task(
                title = "Siapkan Makan Malam",
                description = "Masak ayam panggang dan salad",
                category = "Rumah Tangga",
                priority = TaskPriority.MEDIUM,
                time = LocalTime.of(18, 0),
                colorHex = 0xFFFFEEE5L
            ),
            Task(
                title = "Ganti Sprei",
                description = "Ganti sprei kamar utama dan tamu",
                category = "Kebersihan",
                priority = TaskPriority.LOW,
                time = LocalTime.of(21, 0),
                colorHex = 0xFFE8E4FAL
            )
        )
    )
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
    }

    fun updateTask(task: Task) {
        _tasks.value = _tasks.value.map { existing ->
            if (existing.id == task.id) task else existing
        }
    }

    fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filterNot { it.id == taskId }
    }

    fun getTask(taskId: String?): Task? = _tasks.value.find { it.id == taskId }

    fun todayCount(): Int = _tasks.value.count { it.date == LocalDate.now() }

    fun missedCount(): Int = _tasks.value.count { it.isMissed }

    fun totalCount(): Int = _tasks.value.size

    fun upsertTask(
        taskId: String?,
        state: TaskFormState,
        mode: String
    ) {
        if (mode == "edit" && taskId != null) {
            val existing = getTask(taskId) ?: return
            updateTask(
                existing.copy(
                    title = state.title,
                    description = state.description,
                    category = state.category,
                    priority = state.priority,
                    date = state.date,
                    time = state.time,
                    colorHex = existing.colorHex,
                    isMissed = existing.isMissed
                )
            )
        } else {
            addTask(
                Task(
                    title = state.title,
                    description = state.description,
                    category = state.category.ifBlank { "Lainnya" },
                    priority = state.priority,
                    date = state.date,
                    time = state.time,
                    colorHex = pickColorForCategory(state.category)
                )
            )
        }
    }

    private fun pickColorForCategory(category: String): Long {
        val normalized = category.trim().lowercase()
        return when {
            "kebersihan" in normalized -> 0xFFE8E4FAL
            "pekerjaan" in normalized -> 0xFFE1F3F9L
            "akademik" in normalized -> 0xFFFCE4E9L
            "kesehatan" in normalized -> 0xFFE8F6EDL
            "rumah" in normalized -> 0xFFFFEEE5L
            else -> 0xFFFFEBD6L
        }
    }
}
