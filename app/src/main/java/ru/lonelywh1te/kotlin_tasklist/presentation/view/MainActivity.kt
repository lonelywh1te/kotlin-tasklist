package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityMainBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var taskListFragment = TaskListFragment()
    private var settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.navBottomMenu.selectedItemId = R.id.nav_tasks
        setFragment(taskListFragment)

        binding.navBottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_favourive  -> {
                    setFragment(taskListFragment)
                    true
                }

                R.id.nav_tasks -> {
                    setFragment(taskListFragment)
                    true
                }

                R.id.nav_settings -> {
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