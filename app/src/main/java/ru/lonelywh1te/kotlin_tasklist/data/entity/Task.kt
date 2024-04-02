package ru.lonelywh1te.kotlin_tasklist.data.entity

import android.icu.text.SimpleDateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lonelywh1te.kotlin_tasklist.data.TaskItem
import java.io.Serializable
import java.util.Locale

@Entity(tableName = "task_table")
data class Task (
    val title: String,
    val description: String,
    val isFavourite: Boolean = false,
    val isCompleted: Boolean = false,
    val completionDateInMillis: Long? = null,
    val taskGroupId: Int? = null,

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : TaskItem(), Serializable