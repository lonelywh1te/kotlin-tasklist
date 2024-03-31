package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var task: Task

    private var editMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        task = intent.extras?.getSerializable("task") as Task
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        setContentView(binding.root)
        setTaskData()

        binding.btnDeleteTask.setOnClickListener {
            deleteTask()
        }

        binding.btnEditTask.setOnClickListener {
            binding.inputTaskTitle.setText(task.title)
            binding.inputTaskDescription.setText(task.description)
            binding.cbIsFavourive.isChecked = task.isFavourite

            if (task.completionDateInMillis != null) {
                binding.btnSetTaskCompletionDate.text = Task.normalDateFormat(task.completionDateInMillis!!)
                binding.btnResetTaskDeadline.visibility = View.VISIBLE
            }

            changeActivityMode()
        }

        binding.btnSetTaskCompletionDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val timePicker = TimePickerDialog(this, { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                task.completionDateInMillis = calendar.timeInMillis

                binding.btnResetTaskDeadline.visibility = View.VISIBLE
                binding.btnSetTaskCompletionDate.text = Task.normalDateFormat(task.completionDateInMillis!!)

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

            val datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)

                timePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        binding.btnResetTaskDeadline.setOnClickListener {
            task.completionDateInMillis = null
            binding.btnResetTaskDeadline.visibility = View.GONE
            binding.btnSetTaskCompletionDate.text = "Назначить"
        }

        binding.btnRestoreTaskChanges.setOnClickListener {
            changeActivityMode()
        }

        binding.btnSaveTaskChanges.setOnClickListener {
            val newTaskTitle = binding.inputTaskTitle.text.toString()
            val newTaskDescription = binding.inputTaskDescription.text.toString()
            val newFavouriteState = binding.cbIsFavourive.isChecked

            if (newTaskTitle.isBlank()) {
                binding.inputTaskTitle.error = getString(R.string.enterTitle)
            }
            else {
                updateTask(newTaskTitle, newTaskDescription, newFavouriteState)
                setTaskData()
                changeActivityMode()
            }
        }
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

    private fun changeActivityMode() {
        editMode = !editMode

        binding.editTaskLayout.visibility = if (editMode) View.VISIBLE else View.GONE
        binding.readTaskLayout.visibility = if (editMode) View.GONE else View.VISIBLE
    }

    private fun updateTask(title: String, description: String, isFavourite: Boolean) {
        task = Task(title, description, isFavourite, id = task.id, isCompleted = task.isCompleted, taskGroupId = task.taskGroupId, completionDateInMillis = task.completionDateInMillis)
        taskViewModel.updateTask(task)
    }

    private fun deleteTask() {
        taskViewModel.deleteTask(task)
        finish()
    }
}