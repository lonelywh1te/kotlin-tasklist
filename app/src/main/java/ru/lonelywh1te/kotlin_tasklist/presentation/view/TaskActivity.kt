package ru.lonelywh1te.kotlin_tasklist.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var viewModel: MainViewModel

    private var editMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContentView(binding.root)

        val extras = intent.extras

        var taskTitle = extras?.getString("task_title")
        var taskDescription = extras?.getString("task_desc")
        var taskIsFavourite = extras?.getBoolean("task_isFavourite")
        val taskId = extras?.getInt("task_id")

        setTaskData(taskTitle, taskDescription, taskIsFavourite)

        binding.btnDeleteTask.setOnClickListener {
            if (taskId != null) deleteTask(taskId)
        }

        binding.btnEditTask.setOnClickListener {
            changeTaskMode()

            binding.inputTaskTitle.setText(taskTitle)
            binding.inputTaskDescription.setText(taskDescription)
            if (taskIsFavourite != null) {
                binding.cbIsFavourive.isChecked = taskIsFavourite as Boolean
            }
        }

        binding.btnRestoreTaskChanges.setOnClickListener {
            changeTaskMode()
        }

        binding.btnSaveTaskChanges.setOnClickListener {
            taskTitle = binding.inputTaskTitle.text.toString()
            taskDescription = binding.inputTaskDescription.text.toString()
            taskIsFavourite = binding.cbIsFavourive.isChecked

            setTaskData(taskTitle, taskDescription, taskIsFavourite)
            updateTask(taskId!!, taskTitle!!, taskDescription!!, taskIsFavourite!!)
            changeTaskMode()
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

    private fun changeTaskMode() {
        editMode = !editMode

        when(editMode) {
            false -> {
                binding.editTaskLayout.visibility = View.GONE
                binding.readTaskLayout.visibility = View.VISIBLE
            }

            true -> {
                binding.readTaskLayout.visibility = View.GONE
                binding.editTaskLayout.visibility = View.VISIBLE
            }
        }
    }
    private fun updateTask(id: Int, title: String, desc: String, isFavourite: Boolean) {
        viewModel.updateTask(Task(title, desc, isFavourite, id = id))
    }

    private fun deleteTask(id: Int) {
        viewModel.deleteTask(id)
        finish()
    }
}