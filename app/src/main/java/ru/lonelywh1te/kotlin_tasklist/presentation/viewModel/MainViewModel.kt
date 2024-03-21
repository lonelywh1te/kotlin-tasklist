package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.data.MainDatabase
import ru.lonelywh1te.kotlin_tasklist.data.Task

class MainViewModel(application: Application): AndroidViewModel(application) {
    val taskList = MutableLiveData<List<Task>>()
    private val taskDao = MainDatabase.getDatabase(application).TaskDao()

    fun getTaskList(): List<Task> {
        return if (taskList.value.isNullOrEmpty()) emptyList() else taskList.value!!
    }

    fun getAllTasks() {
        viewModelScope.launch {
            taskList.postValue(taskDao.getAllTasks())
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

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            taskDao.deleteTask(id)
            getAllTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
            getAllTasks()
        }
    }
}