package ru.lonelywh1te.kotlin_tasklist.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        val taskTitle = extras?.getString("task_title")
        val taskDescription = extras?.getString("task_desc")
        val taskIsFavourite = extras?.getBoolean("task_isFavourite")

        setTaskData(taskTitle, taskDescription, taskIsFavourite)
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
}