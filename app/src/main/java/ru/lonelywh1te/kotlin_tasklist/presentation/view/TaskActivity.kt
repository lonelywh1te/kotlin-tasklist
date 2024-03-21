package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var task: Task

    private var editMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        task = intent.extras?.getSerializable("task") as Task
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContentView(binding.root)
        setTaskData()

        binding.btnDeleteTask.setOnClickListener {
            deleteTask()
        }

        binding.btnEditTask.setOnClickListener {
            changeActivityMode()

            binding.inputTaskTitle.setText(task.title)
            binding.inputTaskDescription.setText(task.description)
            binding.cbIsFavourive.isChecked = task.isFavourite
        }

        binding.btnRestoreTaskChanges.setOnClickListener {
            changeActivityMode()
        }

        binding.btnSaveTaskChanges.setOnClickListener {
            val newTaskTitle = binding.inputTaskTitle.text.toString()
            val newTaskDescription = binding.inputTaskDescription.text.toString()
            val newFavouriteState = binding.cbIsFavourive.isChecked

            updateTask(newTaskTitle, newTaskDescription, newFavouriteState)
            setTaskData()
            changeActivityMode()
        }
    }

    private fun setTaskData() {
        binding.tvTaskTitle.text = task.title
        binding.tvTaskDescription.text = task.description

        binding.tvIsFavouriteStatus.visibility = if (task.isFavourite) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun changeActivityMode() {
        editMode = !editMode

        binding.editTaskLayout.visibility = if (editMode) View.VISIBLE else View.GONE
        binding.readTaskLayout.visibility = if (editMode) View.GONE else View.VISIBLE
    }

    private fun updateTask(title: String, description: String, isFavourite: Boolean) {
        task = Task(title, description, isFavourite, id = task.id)
        viewModel.updateTask(task)
    }

    private fun deleteTask() {
        viewModel.deleteTask(task)
        finish()
    }
}