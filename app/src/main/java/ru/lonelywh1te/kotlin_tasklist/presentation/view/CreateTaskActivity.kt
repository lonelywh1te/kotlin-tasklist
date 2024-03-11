package ru.lonelywh1te.kotlin_tasklist.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.lonelywh1te.kotlin_tasklist.R

class CreateTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
    }
}