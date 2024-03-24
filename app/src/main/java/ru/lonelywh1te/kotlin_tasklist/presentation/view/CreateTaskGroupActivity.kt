package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.app.ActivityManager.TaskDescription
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateTaskBinding
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class CreateTaskGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskGroupBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskGroupBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContentView(binding.root)

        binding.btnAddTaskGroup.setOnClickListener {
            val name = binding.inputTaskGroupName.text.toString()
            val description = binding.inputTaskGroupDescription.text.toString()

            createTaskGroup(name, description)
        }
    }

    private fun createTaskGroup(name: String, description: String) {
        viewModel.addTaskGroup(TaskGroup(name, description))
        finish()
    }
}