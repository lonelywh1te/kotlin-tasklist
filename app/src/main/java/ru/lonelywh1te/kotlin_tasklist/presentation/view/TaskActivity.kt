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

        val task_title = extras?.getString("task_title")
        val task_description = extras?.getString("task_desc")
        val task_isFavourite = extras?.getBoolean("task_isFavourite")

        setTaskData(task_title, task_description, task_isFavourite)
    }

    private fun setTaskData(title: String?, desc: String?, isFavourite: Boolean?) {
        binding.tvTaskTitle.text = title
        binding.tvTaskDescription.text = desc

        binding.tvIsFavouriteStatus.visibility = if (isFavourite != null && isFavourite) {
            View.VISIBLE
        }
        else {
            View.GONE
        }
    }
}