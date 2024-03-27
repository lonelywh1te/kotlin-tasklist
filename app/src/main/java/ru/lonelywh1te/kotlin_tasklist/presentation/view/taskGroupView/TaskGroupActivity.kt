package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.data.TaskItem
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.ItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.CreateTaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.TaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class TaskGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskGroupBinding
    private lateinit var recycler: RecyclerView
    private lateinit var taskGroup: TaskGroup

    private lateinit var taskGroupViewModel: TaskGroupViewModel
    private lateinit var taskViewModel: TaskViewModel

    private var editMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskGroupBinding.inflate(layoutInflater)

        taskGroupViewModel = ViewModelProvider(this)[TaskGroupViewModel::class.java]
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        taskGroup = intent.extras?.getSerializable("taskGroup") as TaskGroup
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
            adapter.updateTaskList(taskViewModel.getTaskList())

            binding.tvIsEmptyList.visibility = if (taskViewModel.getTaskList().isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        setContentView(binding.root)
        setTaskGroupData()

        binding.btnDeleteTaskGroup.setOnClickListener {
            deleteTaskGroup()
        }

        binding.btnAddTaskToGroup.setOnClickListener {
            addTask()
        }

        binding.btnEditTaskGroup.setOnClickListener {
            changeActivityMode()

            binding.inputTaskGroupName.setText(taskGroup.name)
            binding.inputTaskGroupDescription.setText(taskGroup.description)
        }

        binding.btnRestoreTaskGroupChanges.setOnClickListener {
            changeActivityMode()
        }

        binding.btnSaveTaskGroupChanges.setOnClickListener {
            val newTaskGroupName = binding.inputTaskGroupName.text.toString()
            val newTaskGroupDescription = binding.inputTaskGroupDescription.text.toString()

            if (newTaskGroupName.isBlank()) {
                binding.inputTaskGroupName.error = "Введите название"
            }
            else {
                updateTaskGroup(newTaskGroupName, newTaskGroupDescription)
                setTaskGroupData()
                changeActivityMode()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.getAllTasks(taskGroup.id)
    }

    private fun addTask() {
        val intent = Intent(this, CreateTaskActivity::class.java)
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

    private fun updateTaskGroup(name: String, description: String) {
        taskGroup = TaskGroup(name, description, id = taskGroup.id)
        taskGroupViewModel.updateTaskGroup(taskGroup)
    }

    private fun deleteTaskGroup() {
        for (task in taskViewModel.getTaskList()) {
            task.taskGroupId = null
            taskViewModel.updateTask(task)
        }

        taskGroupViewModel.deleteTaskGroup(taskGroup)
        finish()
    }

    private fun changeActivityMode() {
        editMode = !editMode

        binding.editTaskGroupLayout.visibility = if (editMode) View.VISIBLE else View.GONE
        binding.readTaskGroupLayout.visibility = if (editMode) View.GONE else View.VISIBLE
    }
}