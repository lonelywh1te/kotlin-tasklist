package ru.lonelywh1te.kotlin_tasklist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert
    suspend fun addTask(task: Task)

    @Query("DELETE FROM task_table WHERE id=:id")
    suspend fun deleteTask(id: Int)

    @Query("SELECT * FROM task_table")
    suspend fun getAllTasks(): List<Task>

}