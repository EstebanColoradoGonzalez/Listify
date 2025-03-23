package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.registerFragment, R.id.userBudgetFragment -> supportActionBar?.hide()
                else -> supportActionBar?.show()
            }

            if (destination.id == R.id.shoppingListsFragment) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }

        userViewModel.user.observe(this) { user ->
            val graph = navController.navInflater.inflate(R.navigation.main_graph)
            if (user == null) {
                graph.setStartDestination(R.id.registerFragment)
            } else {
                graph.setStartDestination(R.id.shoppingListsFragment)
                graph.addArgument(TextConstants.PARAM_USER_ID, androidx.navigation.NavArgument.Builder()
                    .setType(androidx.navigation.NavType.LongType)
                    .setDefaultValue(user.id)
                    .build())
            }
            navController.setGraph(graph, null)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
