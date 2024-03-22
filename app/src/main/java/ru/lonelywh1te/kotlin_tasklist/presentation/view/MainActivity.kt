package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityMainBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var taskListFragment = TaskListFragment(false)
    private var favouriteTaskListFragment = TaskListFragment(true)
    private var settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContentView(binding.root)
        setFragment(taskListFragment)

        binding.navBottomMenu.selectedItemId = R.id.nav_tasks

        binding.navBottomMenu.setOnItemSelectedListener {
            binding.tvActivityTitle.text = it.title

            when(it.itemId) {
                R.id.nav_favourive  -> {
                    binding.btnCreateTask.visibility = View.GONE
                    setFragment(favouriteTaskListFragment)
                    true
                }

                R.id.nav_tasks -> {
                    binding.btnCreateTask.visibility = View.VISIBLE
                    setFragment(taskListFragment)
                    true
                }

                R.id.nav_settings -> {
                    binding.btnCreateTask.visibility = View.GONE
                    setFragment(settingsFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }

        binding.btnCreateTask.setOnClickListener {
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }
    }

    private fun setFragment(fragment: Fragment) {
        binding.navBottomMenu.selectedItemId = fragment.id
        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit()
    }
}