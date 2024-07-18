package ru.lonelywh1te.kotlin_tasklist.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "task_group")
data class TaskGroupEntity (
    val name: String,
    val description: String,

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
