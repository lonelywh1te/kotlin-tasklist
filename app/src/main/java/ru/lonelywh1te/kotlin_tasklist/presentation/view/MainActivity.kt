package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityMainBinding
import ru.lonelywh1te.kotlin_tasklist.databinding.DialogCreateItemBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var taskListFragment = TaskListFragment(false)
    private var favouriteTaskListFragment = TaskListFragment(true)
    private var settingsFragment = SettingsFragment()
    private lateinit var createItemDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        createItemDialog = Dialog(this)

        setContentView(binding.root)
        setFragment(taskListFragment)

        binding.navBottomMenu.selectedItemId = R.id.nav_tasks

        binding.navBottomMenu.setOnItemSelectedListener {
            binding.tvActivityTitle.text = it.title

            when(it.itemId) {
                R.id.nav_favourive  -> {
                    binding.btnCreateItem.visibility = View.GONE
                    setFragment(favouriteTaskListFragment)
                    true
                }

                R.id.nav_tasks -> {
                    binding.btnCreateItem.visibility = View.VISIBLE
                    setFragment(taskListFragment)
                    true
                }

                R.id.nav_settings -> {
                    binding.btnCreateItem.visibility = View.GONE
                    setFragment(settingsFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }

        binding.btnCreateItem.setOnClickListener {
            showCreateItemDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        if (createItemDialog.isShowing) createItemDialog.dismiss()
    }

    private fun setFragment(fragment: Fragment) {
        binding.navBottomMenu.selectedItemId = fragment.id
        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit()
    }

    private fun showCreateItemDialog() {
        val dialogBinding = DialogCreateItemBinding.inflate(layoutInflater)

        createItemDialog.setContentView(dialogBinding.root)
        createItemDialog.setCancelable(true)
        createItemDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnCreateTask.setOnClickListener {
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }

        dialogBinding.btnCreateTaskGroup.setOnClickListener {
            startActivity(Intent(this, CreateTaskGroupActivity::class.java))
        }

        createItemDialog.show()
    }
}