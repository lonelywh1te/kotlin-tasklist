package ru.lonelywh1te.kotlin_tasklist.di

import org.koin.dsl.module
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.GetAllTaskItemsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.CreateTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.DeleteTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetAllTaskGroupsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetTaskGroupByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.UpdateTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.CreateTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.DeleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetAllTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetFavouriteTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetTaskByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

val domainModule = module {
    factory<CreateTaskUseCase> { CreateTaskUseCase(taskRepository = get()) }
    factory<DeleteTaskUseCase> { DeleteTaskUseCase(taskRepository = get()) }
    factory<UpdateTaskUseCase> { UpdateTaskUseCase(taskRepository = get()) }
    factory<GetAllTasksUseCase> { GetAllTasksUseCase(taskRepository = get()) }
    factory<GetFavouriteTasksUseCase> { GetFavouriteTasksUseCase(taskRepository = get()) }
    factory<GetTaskByIdUseCase> { GetTaskByIdUseCase(taskRepository = get()) }

    factory<CreateTaskGroupUseCase> { CreateTaskGroupUseCase(taskGroupRepository = get()) }
    factory<DeleteTaskGroupUseCase> { DeleteTaskGroupUseCase(taskGroupRepository = get()) }
    factory<UpdateTaskGroupUseCase> { UpdateTaskGroupUseCase(taskGroupRepository = get()) }
    factory<GetAllTaskGroupsUseCase> { GetAllTaskGroupsUseCase(taskGroupRepository = get()) }
    factory<GetTaskGroupByIdUseCase> { GetTaskGroupByIdUseCase(taskGroupRepository = get()) }
    factory<GetAllTaskItemsUseCase> { GetAllTaskItemsUseCase(taskRepository = get(), taskGroupRepository = get()) }
}