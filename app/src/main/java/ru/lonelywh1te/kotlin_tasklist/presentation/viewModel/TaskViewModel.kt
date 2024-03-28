package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.data.MainDatabase
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task

class TaskViewModel(app: Application): AndroidViewModel(app) {
    val taskList = MutableLiveData<List<Task>>()
    var isFavouriteTaskList = false

    private val taskDao = MainDatabase.getDatabase(app).TaskDao()

    fun getTaskList(): List<Task> {
        return taskList.value.orEmpty()
    }

    fun getAllTasks() {
        viewModelScope.launch {
            if (isFavouriteTaskList) {
                getFavouriteTasks()
            } else {
                taskList.postValue(taskDao.getAllTasks())
            }
        }
    }

    fun getAllTasks(taskGroupId: Int?) {
        viewModelScope.launch {
            taskList.postValue(taskDao.getAllTasks(taskGroupId))
        }
    }

    fun getFavouriteTasks() {
        viewModelScope.launch {
            taskList.postValue(taskDao.getFavouriteTasks())
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.addTask(task)
            getAllTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
            getAllTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
            getAllTasks()
        }
    }

    fun changeTaskCompletion(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            taskDao.changeTaskCompletion(task.id, isCompleted)

            if (task.taskGroupId == null || isFavouriteTaskList) getAllTasks()
            else getAllTasks(task.taskGroupId)
        }
    }
}