package ru.lonelywh1te.kotlin_tasklist.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateTaskBinding

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}