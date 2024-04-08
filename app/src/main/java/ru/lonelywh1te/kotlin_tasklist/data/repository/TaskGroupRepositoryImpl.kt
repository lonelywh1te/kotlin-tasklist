package ru.lonelywh1te.kotlin_tasklist.data.repository

import ru.lonelywh1te.kotlin_tasklist.data.room.Mapper
import ru.lonelywh1te.kotlin_tasklist.data.room.dao.TaskGroupDao
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskGroupRepository

class TaskGroupRepositoryImpl(private val taskGroupDao: TaskGroupDao): TaskGroupRepository {
    private val mapper = Mapper()

    override suspend fun addTaskGroup(taskGroup: TaskGroup) {
        taskGroupDao.addTaskGroup(mapper.taskGroupToTaskGroupEntity(taskGroup))
    }

    override suspend fun deleteTaskGroup(taskGroup: TaskGroup) {
        taskGroupDao.deleteTaskGroup(mapper.taskGroupToTaskGroupEntity(taskGroup))
    }

    override suspend fun updateTaskGroup(taskGroup: TaskGroup) {
        taskGroupDao.updateTaskGroup(mapper.taskGroupToTaskGroupEntity(taskGroup))
    }

    override suspend fun getAllTaskGroups(): List<TaskGroup> {
        return taskGroupDao.getAllTaskGroups().map {
            mapper.taskGroupEntityToTaskGroup(it)
        }
    }

    override suspend fun getTaskGroupById(id: Int): TaskGroup {
        return mapper.taskGroupEntityToTaskGroup(taskGroupDao.getTaskGroupById(id))
    }
}