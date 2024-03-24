package ru.lonelywh1te.kotlin_tasklist.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateTaskBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val taskGroupId = intent.extras?.getInt("taskGroupId")

        setContentView(binding.root)

        binding.btnAddTask.setOnClickListener {
            val title = binding.inputTaskTitle.text.toString()
            val description = binding.inputTaskDescription.text.toString()
            val isFavourite = binding.cbIsFavourive.isChecked

            createTask(title, description, isFavourite, taskGroupId)
        }
    }

    private fun createTask(title: String, description: String, isFavourite: Boolean, taskGroupId: Int?) {
        viewModel.addTask(Task(title, description, isFavourite, taskGroupId = taskGroupId))
        finish()
    }
}