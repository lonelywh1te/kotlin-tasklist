package ru.lonelywh1te.kotlin_tasklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lonelywh1te.kotlin_tasklist.data.TaskItem
import java.io.Serializable

@Entity(tableName = "task_group")
data class TaskGroup (
    val name: String,
    val description: String,

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : TaskItem(), Serializable
