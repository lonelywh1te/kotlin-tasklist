package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        task = intent.extras?.getSerializable("task") as Task
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        setContentView(binding.root)

        binding.btnDeleteTask.setOnClickListener {
            deleteTask()
        }

        binding.btnEditTask.setOnClickListener {
            val intent = Intent(this, CreateEditTaskActivity::class.java)
            intent.putExtra("editMode", true)
            intent.putExtra("task", task)
            startActivity(intent)
        }

        taskViewModel.task.observe(this) {
            task = it
            setTaskData()
        }
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.getTaskById(task.id)
    }

    private fun setTaskData() {
        binding.tvTaskTitle.text = task.title
        binding.tvTaskDescription.text = task.description

        if (task.completionDateInMillis != null) {
            binding.tvTaskCompletionDate.text = Task.normalDateFormat(task.completionDateInMillis!!)
            binding.tvTaskCompletionDate.visibility = View.VISIBLE
        } else {
            binding.tvTaskCompletionDate.visibility = View.GONE
        }

        binding.tvIsFavouriteStatus.visibility = if (task.isFavourite) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun deleteTask() {
        taskViewModel.deleteTask(task)
        finish()
    }
}