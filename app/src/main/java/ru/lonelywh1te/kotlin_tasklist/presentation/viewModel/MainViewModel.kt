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
    private val taskDao = MainDatabase.getDatabase(application).TaskDao()

    fun getTaskList(): List<Task> {
        if (taskList.value == null) refreshTasks()

        return if (taskList.value.isNullOrEmpty()) emptyList() else taskList.value!!
    }

    fun refreshTasks() {
        viewModelScope.launch {
            taskList.postValue(taskDao.getAllTasks())
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.addTask(task)
            refreshTasks()
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            taskDao.deleteTask(id)
            refreshTasks()
        }
    }
}