package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityMainBinding
import ru.lonelywh1te.kotlin_tasklist.databinding.DialogCreateItemBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView.CreateEditTaskGroupActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.CreateEditTaskActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var itemListFragment = ItemListFragment.newInstance(false)
    private var favouriteItemListFragment = ItemListFragment.newInstance(true)
    private var infoFragment = InfoFragment()
    private lateinit var createItemDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        createItemDialog = Dialog(this)

        setContentView(binding.root)
        setFragment(itemListFragment)

        binding.navBottomMenu.selectedItemId = R.id.nav_tasks

        binding.navBottomMenu.setOnItemSelectedListener {
            binding.tvActivityTitle.text = it.title

            when(it.itemId) {
                R.id.nav_favourive  -> {
                    binding.btnCreateItem.visibility = View.GONE
                    setFragment(favouriteItemListFragment)
                    true
                }

                R.id.nav_tasks -> {
                    binding.btnCreateItem.visibility = View.VISIBLE
                    setFragment(itemListFragment)
                    true
                }

                R.id.nav_settings -> {
                    binding.btnCreateItem.visibility = View.GONE
                    setFragment(infoFragment)
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
            startActivity(Intent(this, CreateEditTaskActivity::class.java))
        }

        dialogBinding.btnCreateTaskGroup.setOnClickListener {
            startActivity(Intent(this, CreateEditTaskGroupActivity::class.java))
        }

        createItemDialog.show()
    }
}