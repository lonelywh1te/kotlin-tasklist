package ru.lonelywh1te.kotlin_tasklist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup

@Dao
interface TaskGroupDao {

    @Insert
    suspend fun addTaskGroup(taskGroup: TaskGroup)

    @Delete
    suspend fun deleteTaskGroup(taskGroup: TaskGroup)

    @Update
    suspend fun updateTaskGroup(taskGroup: TaskGroup)

    @Query("SELECT * from task_group")
    suspend fun getAllTaskGroups(): List<TaskGroup>

    @Query("SELECT * from task_group WHERE id=:id")
    suspend fun getTaskGroupById(id: Int): TaskGroup
}