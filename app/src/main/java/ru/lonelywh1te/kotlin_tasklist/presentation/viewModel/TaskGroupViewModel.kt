package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.data.MainDatabase
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup

class TaskGroupViewModel(app: Application): AndroidViewModel(app) {
    val taskGroup = MutableLiveData<TaskGroup>()
    val taskGroupList = MutableLiveData<List<TaskGroup>>()

    private val db = MainDatabase.getDatabase(app)
    private val taskGroupDao = db.TaskGroupDao()

    fun getTaskGroupList(): List<TaskGroup> {
        return taskGroupList.value.orEmpty()
    }

    fun getAllTaskGroups() {
        viewModelScope.launch {
            taskGroupList.postValue(taskGroupDao.getAllTaskGroups())
        }
    }

    fun getTaskGroupById(id: Int) {
        viewModelScope.launch {
            taskGroup.postValue(taskGroupDao.getTaskGroupById(id))
        }
    }

    fun addTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            taskGroupDao.addTaskGroup(taskGroup)
        }
    }

    fun updateTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            taskGroupDao.updateTaskGroup(taskGroup)
        }
    }

    fun deleteTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            taskGroupDao.deleteTaskGroup(taskGroup)
        }
    }
}