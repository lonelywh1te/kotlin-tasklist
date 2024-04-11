package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.OnItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TASK_GROUP_NAME_EXTRA
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.CreateTaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class TaskGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskGroupBinding
    private lateinit var recycler: RecyclerView
    private lateinit var taskGroup: TaskGroup

    private lateinit var taskGroupViewModel: TaskGroupViewModel
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskGroupBinding.inflate(layoutInflater)

        taskGroupViewModel = getViewModel()
        taskViewModel = getViewModel()

        taskGroup = intent.extras?.getSerializable(TASK_GROUP_NAME_EXTRA) as TaskGroup

        recycler = binding.rvTaskGroupTasks

        val adapter = TaskAdapter(OnItemClickListener(this, taskViewModel))

        recycler.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }

        taskViewModel.taskList.observe(this) {
            adapter.updateTaskList(it)

            binding.tvIsEmptyList.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        binding.btnDeleteTaskGroup.setOnClickListener {
            deleteTaskGroup()
        }

        binding.btnAddTaskToGroup.setOnClickListener {
            addTask()
        }

        setTaskGroupData()
        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        clearEditTextFocus()
        updateTaskGroup(taskGroup)
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.getAllTasks(taskGroup.id)
    }

    private fun updateTaskGroup(newTaskGroup: TaskGroup) {
        val name = binding.tvTaskGroupName.text.toString()
        val desc = binding.tvTaskGroupDescription.text.toString()

        taskGroup = if (newTaskGroup.name != title || newTaskGroup.description != desc){
            newTaskGroup.copy(name = name, description = desc)
        } else {
            newTaskGroup
        }

        taskGroupViewModel.updateTaskGroup(taskGroup)
        setTaskGroupData()
    }

    private fun addTask() {
        val intent = Intent(this, CreateTaskActivity::class.java)
        intent.putExtra("taskGroupId", taskGroup.id)
        startActivity(intent)
    }

    private fun setTaskGroupData() {
        binding.tvTaskGroupName.setText(taskGroup.name)
        binding.tvTaskGroupDescription.setText(taskGroup.description)
    }

    private fun deleteTaskGroup() {
        for (task in taskViewModel.getTaskList()) {
            val updatedTaskEntity = Task(task.title, task.description, task.isFavourite, task.isCompleted, task.completionDateInMillis, null, task.id)
            taskViewModel.updateTask(updatedTaskEntity)
        }

        taskGroupViewModel.deleteTaskGroup(taskGroup)
        finish()
    }

    private fun clearEditTextFocus() {
        binding.tvTaskGroupName.clearFocus()
        binding.tvTaskGroupDescription.clearFocus()
    }
}