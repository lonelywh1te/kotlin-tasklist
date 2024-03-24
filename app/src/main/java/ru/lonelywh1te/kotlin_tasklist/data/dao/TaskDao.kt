package ru.lonelywh1te.kotlin_tasklist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun addTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task_table WHERE taskGroupId IS NULL ORDER BY isCompleted ASC")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM task_table WHERE taskGroupId=:taskGroupId ORDER BY isCompleted ASC")
    suspend fun getAllTasks(taskGroupId: Int?): List<Task>

    @Query("SELECT * FROM task_table WHERE isFavourite=1 ORDER BY isCompleted ASC")
    suspend fun getFavouriteTasks(): List<Task>

    @Query("UPDATE task_table SET isCompleted = :isCompleted WHERE id=:id")
    suspend fun changeTaskCompletion(id: Int, isCompleted: Boolean)
}