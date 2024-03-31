package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateTaskBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel
import java.util.Date
import java.util.Locale

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private var completionDate: Long? = null

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
                binding.inputTaskTitle.error = getString(R.string.enterTitle)
            }
            else {
                createTask(title, description, isFavourite, completionDate, taskGroupId)
            }
        }

        binding.btnSetTaskCompletionDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val timePicker = TimePickerDialog(this, { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                completionDate = calendar.timeInMillis

                binding.btnResetTaskDeadline.visibility = View.VISIBLE
                binding.btnSetTaskCompletionDate.text = changeDateFormat(completionDate!!, "dd.MM.yyyy HH:mm")

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

            val datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)

                timePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        binding.btnResetTaskDeadline.setOnClickListener {
            completionDate = null
            binding.btnResetTaskDeadline.visibility = View.GONE
            binding.btnSetTaskCompletionDate.text = "Назначить"
        }
    }

    private fun changeDateFormat(time: Long, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(time)
    }

    private fun createTask(title: String, description: String, isFavourite: Boolean, completionDate: Long?, taskGroupId: Int?) {
        taskViewModel.addTask(Task(title, description, isFavourite, completionDateInMillis = completionDate, taskGroupId = taskGroupId))
        finish()
    }
}