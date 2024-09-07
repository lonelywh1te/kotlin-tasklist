package ru.lonelywh1te.kotlin_tasklist.data.room

import ru.lonelywh1te.kotlin_tasklist.data.room.entity.TaskEntity
import ru.lonelywh1te.kotlin_tasklist.data.room.entity.TaskGroupEntity
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup

class Mapper {
    fun taskToTaskEntity(task: Task): TaskEntity {
        return TaskEntity(
            title = task.title,
            description = task.description,
            isFavourite = task.isFavourite,
            isCompleted = task.isCompleted,
            completionDateInMillis = task.completionDateInMillis,
            taskGroupId = task.taskGroupId,
            order = task.order,
            id = task.id,
        )
    }

    fun taskEntityToTask(taskEntity: TaskEntity): Task {
        return Task(
            title = taskEntity.title,
            description = taskEntity.description,
            isFavourite = taskEntity.isFavourite,
            isCompleted = taskEntity.isCompleted,
            completionDateInMillis = taskEntity.completionDateInMillis,
            taskGroupId = taskEntity.taskGroupId,
            order = taskEntity.order,
            id = taskEntity.id
        )
    }

    fun taskGroupToTaskGroupEntity(taskGroup: TaskGroup): TaskGroupEntity {
        return TaskGroupEntity(
            name = taskGroup.name,
            description = taskGroup.description,
            order = taskGroup.order,
            id = taskGroup.id
        )
    }

    fun taskGroupEntityToTaskGroup(taskGroupEntity: TaskGroupEntity): TaskGroup {
        return TaskGroup(
            name = taskGroupEntity.name,
            description = taskGroupEntity.description,
            order = taskGroupEntity.order,
            id = taskGroupEntity.id
        )
    }
}