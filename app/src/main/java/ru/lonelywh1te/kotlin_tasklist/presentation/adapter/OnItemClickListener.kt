package ru.lonelywh1te.kotlin_tasklist.presentation.adapter

import android.content.Context
import android.content.Intent
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView.TaskGroupActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.TaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class OnItemClickListener(private val context: Context, private val taskViewModel: TaskViewModel): ItemClickListener {
    override fun onItemClicked(taskItem: TaskItem) {
        val intent: Intent = when(taskItem) {
            is Task -> {
                Intent(context, TaskActivity::class.java).apply {
                    putExtra("task", taskItem)
                }
            }
            is TaskGroup -> {
                Intent(context, TaskGroupActivity::class.java).apply {
                    putExtra("taskGroup", taskItem)
                }
            }
            else -> throw IllegalAccessException("Invalid View Type")
        }

        context.startActivity(intent);
    }

    override fun onTaskCheckboxClicked(task: Task, isCompleted: Boolean) {
        taskViewModel.changeTaskCompletion(task, isCompleted)
    }
}