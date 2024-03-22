package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.data.MainDatabase
import ru.lonelywh1te.kotlin_tasklist.data.Task

class MainViewModel(application: Application): AndroidViewModel(application) {
    val taskList = MutableLiveData<List<Task>>()
    var isFavouriteTaskList = false
    private val taskDao = MainDatabase.getDatabase(application).TaskDao()

    fun getTaskList(): List<Task> {
        return if (taskList.value.isNullOrEmpty()) emptyList() else taskList.value!!
    }

    fun getAllTasks() {
        viewModelScope.launch {
            if (isFavouriteTaskList) getFavouriteTasks()
            else taskList.postValue(taskDao.getAllTasks())
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

    fun changeTaskCompletion(id: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            taskDao.changeTaskCompletion(id, isCompleted)
            getAllTasks()
        }
    }
}