package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.CreateTaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.TaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class TaskGroupActivity : AppCompatActivity(), TaskClickListener {
    private lateinit var binding: ActivityTaskGroupBinding
    private lateinit var recycler: RecyclerView
    private lateinit var taskGroup: TaskGroup
    private lateinit var viewModel: MainViewModel

    private var editMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskGroupBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        taskGroup = intent.extras?.getSerializable("taskGroup") as TaskGroup
        recycler = binding.rvTaskGroupTasks

        val adapter = TaskAdapter(this)

        recycler.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.taskList.observe(this) {
            adapter.updateTaskList(viewModel.getTaskList())
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

            updateTaskGroup(newTaskGroupName, newTaskGroupDescription)
            setTaskGroupData()
            changeActivityMode()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllTasks(taskGroup.id)
    }

    private fun addTask() {
        val intent = Intent(this, CreateTaskActivity::class.java)
        intent.putExtra("taskGroupId", taskGroup.id)
        startActivity(intent)
    }

    private fun setTaskGroupData() {
        binding.tvTaskGroupName.text = taskGroup.name
        binding.tvTaskGroupDescription.text = taskGroup.description
    }

    private fun updateTaskGroup(name: String, description: String) {
        taskGroup = TaskGroup(name, description, id = taskGroup.id)
        viewModel.updateTaskGroup(taskGroup)
    }

    private fun deleteTaskGroup() {
        for (task in viewModel.getTaskList()) {
            task.taskGroupId = null
            viewModel.updateTask(task)
        }

        viewModel.deleteTaskGroup(taskGroup)
        finish()
    }

    private fun changeActivityMode() {
        editMode = !editMode

        binding.editTaskGroupLayout.visibility = if (editMode) View.VISIBLE else View.GONE
        binding.readTaskGroupLayout.visibility = if (editMode) View.GONE else View.VISIBLE
    }

    override fun onTaskClicked(task: Task) {
        val intent = Intent(binding.root.context, TaskActivity::class.java)
        intent.putExtra("task", task)

        binding.root.context.startActivity(intent);
    }

    override fun onTaskCheckboxClicked(task: Task, isCompleted: Boolean) {
        viewModel.changeTaskCompletion(task, isCompleted)
    }
}