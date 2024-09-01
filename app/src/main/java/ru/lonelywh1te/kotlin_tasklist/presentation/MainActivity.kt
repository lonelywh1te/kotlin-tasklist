package ru.lonelywh1te.kotlin_tasklist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.tasks_fragment, R.id.info_fragment, R.id.favourite_tasks_fragment))

        binding.toolBar.setupWithNavController(navController, appBarConfiguration)
        binding.navBottomMenu.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val visibleFragments = setOf(R.id.tasks_fragment, R.id.info_fragment, R.id.favourite_tasks_fragment)
            binding.navBottomMenu.visibility = if (destination.id in visibleFragments) View.VISIBLE else View.GONE

            setSupportActionBar(binding.toolBar)
            setContentView(binding.root)
        }
    }

}