package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.data.MainDatabase
import ru.lonelywh1te.kotlin_tasklist.data.TaskItem
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup

class MainViewModel(application: Application): AndroidViewModel(application) {
    val taskList = MutableLiveData<List<Task>>()
    val taskGroupList = MutableLiveData<List<TaskGroup>>()

    var isFavouriteTaskList = false

    private val db = MainDatabase.getDatabase(application)
    private val taskDao = db.TaskDao()
    private val taskGroupDao = db.TaskGroupDao()

    fun getTaskList(): List<Task> {
        return taskList.value.orEmpty()
    }

    fun getTaskGroupList(): List<TaskGroup> {
        return taskGroupList.value.orEmpty()
    }

    fun getAllItems() {
        getAllTasks()
        getAllTaskGroups()
    }

    fun getAllTasks() {
        viewModelScope.launch {
            if (isFavouriteTaskList) {
                getFavouriteTasks()
            }
            else {
                taskList.postValue(taskDao.getAllTasks())
            }
        }
    }

    fun getAllTasks(taskGroupId: Int?) {
        viewModelScope.launch {
            taskList.postValue(taskDao.getAllTasks(taskGroupId))
            Log.println(Log.DEBUG, "VIEW_MODEL", "${taskDao.getAllTasks(taskGroupId)} | $taskGroupId")
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

            if (task.taskGroupId == null) getAllTasks()
            else getAllTasks(task.taskGroupId)
        }
    }

    fun addTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            taskGroupDao.addTaskGroup(taskGroup)
        }
    }

    fun getAllTaskGroups() {
        viewModelScope.launch {
            taskGroupList.postValue(taskGroupDao.getAllTaskGroups())
        }
    }

    fun deleteTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            taskGroupDao.deleteTaskGroup(taskGroup)
        }
    }
}