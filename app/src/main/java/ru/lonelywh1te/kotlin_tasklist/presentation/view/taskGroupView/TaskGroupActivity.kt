package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.TaskItem
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.ItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.CreateEditTaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.TaskActivity
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
        setContentView(binding.root)

        taskGroupViewModel = ViewModelProvider(this)[TaskGroupViewModel::class.java]
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        taskGroup = intent.extras?.getSerializable("taskGroup") as TaskGroup

        taskGroupViewModel.taskGroup.observe(this) {
            taskGroup = it
            setTaskGroupData()
        }

        recycler = binding.rvTaskGroupTasks

        val adapter = TaskAdapter(object: ItemClickListener {
            override fun onItemClicked(taskItem: TaskItem) {
                val intent = Intent(binding.root.context, TaskActivity::class.java)
                intent.putExtra("task", taskItem as Task)

                binding.root.context.startActivity(intent);
            }

            override fun onTaskCheckboxClicked(task: Task, isCompleted: Boolean) {
                taskViewModel.changeTaskCompletion(task, isCompleted)
            }
        })

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

        binding.btnEditTaskGroup.setOnClickListener {
            val intent = Intent(this, CreateEditTaskGroupActivity::class.java)
            intent.putExtra("editMode", true)
            intent.putExtra("taskGroup", taskGroup)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        taskGroupViewModel.getTaskGroupById(taskGroup.id)
        taskViewModel.getAllTasks(taskGroup.id)
    }

    private fun addTask() {
        val intent = Intent(this, CreateEditTaskActivity::class.java)
        intent.putExtra("taskGroupId", taskGroup.id)
        startActivity(intent)
    }

    private fun setTaskGroupData() {
        binding.tvTaskGroupName.text = taskGroup.name
        binding.tvTaskGroupDescription.text = taskGroup.description

        binding.tvTaskGroupDescription.visibility = if (binding.tvTaskGroupDescription.text.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun deleteTaskGroup() {
        for (task in taskViewModel.getTaskList()) {
            val updatedTask = Task(task.title, task.description, task.isFavourite, task.isCompleted, task.completionDateInMillis, null, task.id)
            taskViewModel.updateTask(updatedTask)
        }

        taskGroupViewModel.deleteTaskGroup(taskGroup)
        finish()
    }
}