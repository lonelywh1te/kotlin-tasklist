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
        if (taskList.value == null){
            refreshTasks()
        }

        return if (taskList.value.isNullOrEmpty()) emptyList() else taskList.value!!
    }

    private fun refreshTasks() {
        viewModelScope.launch {
            Log.println(Log.DEBUG, "view_model", "taskDao return: ${taskDao.getAllTasks()}")
            taskList.postValue(taskDao.getAllTasks())
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.addTask(task)
            refreshTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
            refreshTasks()
        }
    }
}