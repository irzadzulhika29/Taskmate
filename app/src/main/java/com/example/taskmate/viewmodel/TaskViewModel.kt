package com.example.taskmate.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

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
    val dateMillis: Long = System.currentTimeMillis(),
    val timeHour: Int,
    val timeMinute: Int,
    val colorHex: Long,
    val isMissed: Boolean = false
) {
    fun getTimeString(): String {
        return String.format(Locale.getDefault(), "%02d:%02d", timeHour, timeMinute)
    }

    fun getDateString(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date(dateMillis))
    }
}

data class TaskFormState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val priority: TaskPriority = TaskPriority.HIGH,
    val dateMillis: Long = System.currentTimeMillis(),
    val timeHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    val timeMinute: Int = Calendar.getInstance().get(Calendar.MINUTE)
)

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(
        listOf(
            Task(
                title = "Buang Sampah",
                description = "Kumpulkan sampah rumah dan buang ke TPS",
                category = "Kebersihan",
                priority = TaskPriority.HIGH,
                timeHour = 7,
                timeMinute = 0,
                colorHex = 0xFFFFF6DAL,
                isMissed = true
            ),
            Task(
                title = "Cek Email",
                description = "Balas email penting dari dosen",
                category = "Pekerjaan",
                priority = TaskPriority.MEDIUM,
                timeHour = 9,
                timeMinute = 50,
                colorHex = 0xFFE1F3F9L
            ),
            Task(
                title = "Selesaikan Metopen",
                description = "Lengkapi bab 3 metodologi",
                category = "Akademik",
                priority = TaskPriority.HIGH,
                timeHour = 15,
                timeMinute = 0,
                colorHex = 0xFFFCE4E9L,
                isMissed = true
            ),
            Task(
                title = "Olahraga Ringan",
                description = "Lari 20 menit di sekitar kompleks",
                category = "Kesehatan",
                priority = TaskPriority.LOW,
                timeHour = 17,
                timeMinute = 30,
                colorHex = 0xFFE8F6EDL
            ),
            Task(
                title = "Siapkan Makan Malam",
                description = "Masak ayam panggang dan salad",
                category = "Rumah Tangga",
                priority = TaskPriority.MEDIUM,
                timeHour = 18,
                timeMinute = 0,
                colorHex = 0xFFFFEEE5L
            ),
            Task(
                title = "Ganti Sprei",
                description = "Ganti sprei kamar utama dan tamu",
                category = "Kebersihan",
                priority = TaskPriority.LOW,
                timeHour = 21,
                timeMinute = 0,
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

    fun todayCount(): Int {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        val todayMillis = today.timeInMillis

        val tomorrow = Calendar.getInstance()
        tomorrow.timeInMillis = todayMillis + 24 * 60 * 60 * 1000

        return _tasks.value.count { task ->
            task.dateMillis >= todayMillis && task.dateMillis < tomorrow.timeInMillis
        }
    }

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
                    dateMillis = state.dateMillis,
                    timeHour = state.timeHour,
                    timeMinute = state.timeMinute,
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
                    dateMillis = state.dateMillis,
                    timeHour = state.timeHour,
                    timeMinute = state.timeMinute,
                    colorHex = pickColorForCategory(state.category)
                )
            )
        }
    }

    private fun pickColorForCategory(category: String): Long {
        return when (category) {
            "Kebersihan" -> 0xFFFFF6DAL
            "Pekerjaan" -> 0xFFE1F3F9L
            "Akademik" -> 0xFFFCE4E9L
            "Kesehatan" -> 0xFFE8F6EDL
            "Rumah Tangga" -> 0xFFFFEEE5L
            else -> 0xFFE8E4FAL
        }
    }
}

