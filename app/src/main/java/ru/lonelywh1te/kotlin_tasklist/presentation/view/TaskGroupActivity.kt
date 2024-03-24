package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class TaskGroupActivity : AppCompatActivity(), TaskClickListener {
    private lateinit var binding: ActivityTaskGroupBinding
    private lateinit var recycler: RecyclerView
    private lateinit var taskGroup: TaskGroup
    private lateinit var viewModel: MainViewModel

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

    private fun deleteTaskGroup() {
        viewModel.deleteTaskGroup(taskGroup)
        finish()
    }

    override fun onTaskClicked(task: Task) {
        val intent = Intent(binding.root.context, TaskActivity::class.java)
        intent.putExtra("task", task)

        binding.root.context.startActivity(intent);
    }

    override fun onTaskCheckboxClicked(id: Int, isCompleted: Boolean) {
        viewModel.changeTaskCompletion(id, isCompleted)
    }
}