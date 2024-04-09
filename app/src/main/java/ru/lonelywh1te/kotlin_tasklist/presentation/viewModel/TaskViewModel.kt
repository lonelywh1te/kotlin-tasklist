package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.AddTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.DeleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetAllTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetFavouriteTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetTaskByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

class TaskViewModel(
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getFavouriteTasksUseCase: GetFavouriteTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
): ViewModel() {
    val task = MutableLiveData<Task>()
    val taskList = MutableLiveData<List<Task>>()

    var isFavouriteTaskList = false

    fun getTaskList(): List<Task> {
        return taskList.value.orEmpty()
    }

    fun getAllTasks() {
        viewModelScope.launch {
            if (isFavouriteTaskList) {
                getFavouriteTasks()
            } else {
                taskList.postValue(getAllTasksUseCase.execute())
            }
        }
    }

    fun getAllTasks(taskGroupId: Int?) {
        viewModelScope.launch {
            taskList.postValue(getAllTasksUseCase.execute(taskGroupId))
        }
    }

    fun getTaskById(id: Int) {
        viewModelScope.launch {
            task.postValue(getTaskByIdUseCase.execute(id))
        }
    }

    fun getFavouriteTasks() {
        viewModelScope.launch {
            taskList.postValue(getFavouriteTasksUseCase.execute())
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            Log.println(Log.DEBUG, "kotlin-tasklist", "TASKLIST_BEFORE_EXECUTE: ${taskList.value}")
            addTaskUseCase.execute(task)
            getAllTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase.execute(task)
            getAllTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase.execute(task)

            if (task.taskGroupId == null || isFavouriteTaskList) getAllTasks()
            else getAllTasks(task.taskGroupId)
        }
    }

    fun changeTaskCompletion(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            val changedTask = Task(task.title, task.description, task.isFavourite, isCompleted, task.completionDateInMillis, task.taskGroupId, task.id)
            updateTask(changedTask)
        }
    }
}