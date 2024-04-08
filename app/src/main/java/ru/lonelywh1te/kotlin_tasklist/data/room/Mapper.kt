package ru.lonelywh1te.kotlin_tasklist.data.room

import ru.lonelywh1te.kotlin_tasklist.data.room.entity.TaskEntity
import ru.lonelywh1te.kotlin_tasklist.data.room.entity.TaskGroupEntity
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup

class Mapper {
    fun taskToTaskEntity(task: Task): TaskEntity {
        return TaskEntity(
            task.title,
            task.description,
            task.isFavourite,
            task.isCompleted,
            task.completionDateInMillis,
            task.taskGroupId,
            task.id,
        )
    }

    fun taskEntityToTask(taskEntity: TaskEntity): Task {
        return Task(
            taskEntity.title,
            taskEntity.description,
            taskEntity.isFavourite,
            taskEntity.isCompleted,
            taskEntity.completionDateInMillis,
            taskEntity.taskGroupId,
            taskEntity.id,
        )
    }

    fun taskGroupToTaskGroupEntity(taskGroup: TaskGroup): TaskGroupEntity {
        return TaskGroupEntity(taskGroup.name, taskGroup.description, taskGroup.id)
    }

    fun taskGroupEntityToTaskGroup(taskGroupEntity: TaskGroupEntity): TaskGroup {
        return TaskGroup(taskGroupEntity.name, taskGroupEntity.description, taskGroupEntity.id)
    }
}