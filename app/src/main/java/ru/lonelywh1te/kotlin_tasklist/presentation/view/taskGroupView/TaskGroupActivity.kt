package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.OnItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.CreateEditTaskActivity
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

        setContentView(binding.root)

        taskGroup = intent.extras?.getSerializable("taskGroup") as TaskGroup

        taskGroupViewModel.taskGroup.observe(this) {
            taskGroup = it
            setTaskGroupData()
        }

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
            val updatedTaskEntity = Task(task.title, task.description, task.isFavourite, task.isCompleted, task.completionDateInMillis, null, task.id)
            taskViewModel.updateTask(updatedTaskEntity)
        }

        taskGroupViewModel.deleteTaskGroup(taskGroup)
        finish()
    }
}