package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.DeleteTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetTaskGroupByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.UpdateTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.DeleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetAllTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

class TaskGroupFragmentViewModel(
    private val getTaskGroupByIdUseCase: GetTaskGroupByIdUseCase,
    private val updateTaskGroupUseCase: UpdateTaskGroupUseCase,
    private val deleteTaskGroupUseCase: DeleteTaskGroupUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
): ViewModel(), TaskCompleter {
    val taskGroup = MutableLiveData<TaskGroup>()
    val tasks = MutableLiveData<List<Task>>()
    private val dispatcher = Dispatchers.IO

    fun getTaskGroupById(id: Int) {
        viewModelScope.launch (dispatcher) {
            taskGroup.postValue(getTaskGroupByIdUseCase.execute(id))
        }
    }

    fun updateTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch (dispatcher) {
            updateTaskGroupUseCase.execute(taskGroup)
        }
    }

    fun deleteTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch (dispatcher) {
            deleteTaskGroupIds().join()
            deleteTaskGroupUseCase.execute(taskGroup)
        }
    }

    fun getAllTasksById(taskGroupId: Int) {
        viewModelScope.launch (dispatcher) {
            tasks.postValue(getAllTasksUseCase.execute(taskGroupId))
        }
    }

    fun deleteTasks() {
        viewModelScope.launch (dispatcher) {
            tasks.value?.forEach { deleteTaskUseCase.execute(it) }
        }
    }

    private suspend fun deleteTaskGroupIds() = viewModelScope.launch(dispatcher) {
        tasks.value?.forEach { updateTaskUseCase.execute(it.copy(taskGroupId = null)) }
    }

    override fun completeTask(task: Task) {
        viewModelScope.launch (dispatcher){
            updateTaskUseCase.execute(task.copy(isCompleted = !task.isCompleted))
            getAllTasksById(task.taskGroupId!!)
        }
    }
}