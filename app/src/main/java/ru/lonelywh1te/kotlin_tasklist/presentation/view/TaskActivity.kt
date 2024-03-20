package ru.lonelywh1te.kotlin_tasklist.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContentView(binding.root)

        val extras = intent.extras

        val taskTitle = extras?.getString("task_title")
        val taskDescription = extras?.getString("task_desc")
        val taskIsFavourite = extras?.getBoolean("task_isFavourite")
        val taskId = extras?.getInt("task_id")

        setTaskData(taskTitle, taskDescription, taskIsFavourite)

        binding.btnDeleteTask.setOnClickListener {
            if (taskId != null) deleteTask(taskId)
        }
    }

    private fun setTaskData(title: String?, desc: String?, isFavourite: Boolean?) {
        binding.tvTaskTitle.text = title
        binding.tvTaskDescription.text = desc

        binding.tvIsFavouriteStatus.visibility = if (isFavourite != null && isFavourite) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun deleteTask(id: Int) {
        viewModel.deleteTask(id)
        finish()
    }
}