package ru.lonelywh1te.kotlin_tasklist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert
    suspend fun addTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table")
    suspend fun getAllTasks(): List<Task>

}