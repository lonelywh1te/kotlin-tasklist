package ru.lonelywh1te.kotlin_tasklist.domain.repository

import ru.lonelywh1te.kotlin_tasklist.domain.models.Task

interface TaskRepository {
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)

    suspend fun getAllTasks(): List<Task>
    suspend fun getAllTasksFromGroup(taskGroupId: Int?): List<Task>
    suspend fun getFavouriteTasks(): List<Task>
    suspend fun getTaskById(id: Int): Task
}