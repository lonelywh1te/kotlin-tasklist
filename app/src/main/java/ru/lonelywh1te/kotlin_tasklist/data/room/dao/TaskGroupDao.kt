package ru.lonelywh1te.kotlin_tasklist.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.google.android.material.circularreveal.CircularRevealHelper.Strategy
import ru.lonelywh1te.kotlin_tasklist.data.room.entity.TaskGroupEntity

@Dao
interface TaskGroupDao {

    @Insert
    suspend fun createTaskGroup(taskGroupEntity: TaskGroupEntity)

    @Delete
    suspend fun deleteTaskGroup(taskGroupEntity: TaskGroupEntity)

    @Update
    suspend fun updateTaskGroup(taskGroupEntity: TaskGroupEntity)

    @Query("SELECT * from task_group")
    suspend fun getAllTaskGroups(): List<TaskGroupEntity>

    @Query("SELECT * from task_group WHERE id=:id")
    suspend fun getTaskGroupById(id: Int): TaskGroupEntity
}