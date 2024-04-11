package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel

class CreateTaskGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskGroupBinding
    private lateinit var taskGroupViewModel: TaskGroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskGroupBinding.inflate(layoutInflater)
        taskGroupViewModel = getViewModel()

        binding.btnAddTaskGroup.setOnClickListener {
            val name = binding.inputTaskGroupName.text.toString()
            val description = binding.inputTaskGroupDescription.text.toString()

            if (name.isBlank()) {
                binding.inputTaskGroupName.error = getString(R.string.enterName)
                return@setOnClickListener
            }

            addTaskGroup(name, description)
        }

        setContentView(binding.root)
    }

    private fun addTaskGroup(name: String, description: String) {
        taskGroupViewModel.addTaskGroup(TaskGroup(name, description))
        finish()
    }
}