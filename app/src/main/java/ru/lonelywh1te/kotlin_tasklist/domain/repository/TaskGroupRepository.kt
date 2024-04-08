package ru.lonelywh1te.kotlin_tasklist.domain.repository

import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup

interface TaskGroupRepository {
    suspend fun addTaskGroup(taskGroup: TaskGroup)
    suspend fun deleteTaskGroup(taskGroup: TaskGroup)
    suspend fun updateTaskGroup(taskGroup: TaskGroup)

    suspend fun getAllTaskGroups(): List<TaskGroup>
    suspend fun getTaskGroupById(id: Int): TaskGroup
}