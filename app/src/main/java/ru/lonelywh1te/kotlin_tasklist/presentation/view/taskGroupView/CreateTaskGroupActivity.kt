package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel

class CreateTaskGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskGroupBinding
    private lateinit var taskGroupViewModel: TaskGroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskGroupBinding.inflate(layoutInflater)
        taskGroupViewModel = ViewModelProvider(this)[TaskGroupViewModel::class.java]

        setContentView(binding.root)

        binding.btnAddTaskGroup.setOnClickListener {
            val name = binding.inputTaskGroupName.text.toString()
            val description = binding.inputTaskGroupDescription.text.toString()

            if (name.isBlank()) {
                binding.inputTaskGroupName.error = "Введите название"
            }
            else {
                createTaskGroup(name, description)
            }
        }
    }

    private fun createTaskGroup(name: String, description: String) {
        taskGroupViewModel.addTaskGroup(TaskGroup(name, description))
        finish()
    }
}