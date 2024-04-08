package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.AddTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.DeleteTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetAllTaskGroupsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetTaskGroupByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.UpdateTaskGroupUseCase

class TaskGroupViewModel(
    private val addTaskGroupUseCase: AddTaskGroupUseCase,
    private val deleteTaskGroupUseCase: DeleteTaskGroupUseCase,
    private val updateTaskGroupUseCase: UpdateTaskGroupUseCase,
    private val getAllTaskGroupUseCase: GetAllTaskGroupsUseCase,
    private val getTaskGroupByIdUseCase: GetTaskGroupByIdUseCase
): ViewModel() {
    val taskGroup = MutableLiveData<TaskGroup>()
    val taskGroupList = MutableLiveData<List<TaskGroup>>()

    fun getTaskGroupList(): List<TaskGroup> {
        return taskGroupList.value.orEmpty()
    }

    fun getAllTaskGroups() {
        viewModelScope.launch {
            taskGroupList.postValue(getAllTaskGroupUseCase.execute())
        }
    }

    fun getTaskGroupById(id: Int) {
        viewModelScope.launch {
            taskGroup.postValue(getTaskGroupByIdUseCase.execute(id))
        }
    }

    fun addTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            addTaskGroupUseCase.execute(taskGroup)
        }
    }

    fun updateTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            updateTaskGroupUseCase.execute(taskGroup)
        }
    }

    fun deleteTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            deleteTaskGroupUseCase.execute(taskGroup)
        }
    }
}