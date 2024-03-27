package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateTaskBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        val taskGroupId = intent.extras?.getInt("taskGroupId")

        setContentView(binding.root)

        binding.btnAddTask.setOnClickListener {
            val title = binding.inputTaskTitle.text.toString()
            val description = binding.inputTaskDescription.text.toString()
            val isFavourite = binding.cbIsFavourive.isChecked

            if (title.isBlank()) {
                binding.inputTaskTitle.error = "Введите заголовок"
            }
            else {
                createTask(title, description, isFavourite, taskGroupId)
            }
        }
    }

    private fun createTask(title: String, description: String, isFavourite: Boolean, taskGroupId: Int?) {
        taskViewModel.addTask(Task(title, description, isFavourite, taskGroupId = taskGroupId))
        finish()
    }
}