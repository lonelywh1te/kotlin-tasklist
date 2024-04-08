package ru.lonelywh1te.kotlin_tasklist.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class TaskEntity (
    val title: String,
    val description: String,
    val isFavourite: Boolean = false,
    val isCompleted: Boolean = false,
    val completionDateInMillis: Long? = null,
    val taskGroupId: Int? = null,

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)