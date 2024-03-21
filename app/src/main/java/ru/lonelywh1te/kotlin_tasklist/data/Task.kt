package ru.lonelywh1te.kotlin_tasklist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task_table")
data class Task (
    var title: String,
    var description: String,
    var isFavourite: Boolean = false,
    var isCompleted: Boolean = false,

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : Serializable
