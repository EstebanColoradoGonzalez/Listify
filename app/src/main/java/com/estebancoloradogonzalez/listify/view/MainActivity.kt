package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
        observeUser()
        handleBackPress()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            handleDestinationChanged(destination.id, bottomNavigationView)
        }
    }

    private fun handleDestinationChanged(destinationId: Int, bottomNavigationView: BottomNavigationView) {
        when (destinationId) {
            R.id.registerFragment, R.id.userBudgetFragment -> {
                supportActionBar?.hide()
                bottomNavigationView.visibility = View.GONE
            }
            else -> {
                supportActionBar?.show()
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
        if (destinationId in listOf(
                R.id.shoppingListsFragment,
                R.id.productsFragment,
                R.id.settingsFragment
            )
        ) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun observeUser() {
        userViewModel.user.observe(this) { user ->
            val graph = navController.navInflater.inflate(R.navigation.main_graph)
            if (user == null) {
                graph.setStartDestination(R.id.registerFragment)
            } else {
                graph.setStartDestination(R.id.shoppingListsFragment)
                graph.addArgument(
                    TextConstants.PARAM_USER_ID,
                    NavArgument.Builder()
                        .setType(NavType.LongType)
                        .setDefaultValue(user.id)
                        .build()
                )
            }
            navController.setGraph(graph, null)
        }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val id = navController.currentDestination?.id
                if (id in listOf(
                        R.id.shoppingListsFragment,
                        R.id.userBudgetFragment,
                        R.id.productsFragment,
                        R.id.settingsFragment
                    )
                ) {
                    return
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}