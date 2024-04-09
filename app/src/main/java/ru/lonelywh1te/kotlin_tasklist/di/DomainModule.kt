package ru.lonelywh1te.kotlin_tasklist.di

import org.koin.dsl.module
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.AddTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.DeleteTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetAllTaskGroupsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetTaskGroupByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.UpdateTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.AddTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.DeleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetAllTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetFavouriteTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetTaskByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

val domainModule = module {
    factory<AddTaskUseCase> { AddTaskUseCase(taskRepository = get()) }
    factory<DeleteTaskUseCase> { DeleteTaskUseCase(taskRepository = get()) }
    factory<UpdateTaskUseCase> { UpdateTaskUseCase(taskRepository = get()) }
    factory<GetAllTasksUseCase> { GetAllTasksUseCase(taskRepository = get()) }
    factory<GetFavouriteTasksUseCase> { GetFavouriteTasksUseCase(taskRepository = get()) }
    factory<GetTaskByIdUseCase> { GetTaskByIdUseCase(taskRepository = get()) }

    factory<AddTaskGroupUseCase> { AddTaskGroupUseCase(taskGroupRepository = get()) }
    factory<DeleteTaskGroupUseCase> { DeleteTaskGroupUseCase(taskGroupRepository = get()) }
    factory<UpdateTaskGroupUseCase> { UpdateTaskGroupUseCase(taskGroupRepository = get()) }
    factory<GetAllTaskGroupsUseCase> { GetAllTaskGroupsUseCase(taskGroupRepository = get()) }
    factory<GetTaskGroupByIdUseCase> { GetTaskGroupByIdUseCase(taskGroupRepository = get()) }
}