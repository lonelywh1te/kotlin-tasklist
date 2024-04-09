package ru.lonelywh1te.kotlin_tasklist.di

import org.koin.dsl.module
import ru.lonelywh1te.kotlin_tasklist.data.repository.TaskGroupRepositoryImpl
import ru.lonelywh1te.kotlin_tasklist.data.repository.TaskRepositoryImpl
import ru.lonelywh1te.kotlin_tasklist.data.room.MainDatabase
import ru.lonelywh1te.kotlin_tasklist.data.room.dao.TaskDao
import ru.lonelywh1te.kotlin_tasklist.data.room.dao.TaskGroupDao
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskGroupRepository
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskRepository

val dataModule = module {
    single<MainDatabase> {
        MainDatabase.getDatabase(context = get())
    }

    single<TaskDao> {
        get<MainDatabase>().TaskDao()
    }

    single<TaskGroupDao> {
        get<MainDatabase>().TaskGroupDao()
    }

    single<TaskRepository> {
        TaskRepositoryImpl(taskDao = get())
    }

    single<TaskGroupRepository>{
        TaskGroupRepositoryImpl(taskGroupDao = get())
    }
}