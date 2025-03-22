package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.activity.viewModels
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel.user.observe(this) { user ->
            if (user == null) {
                startActivity(Intent(this, RegisterActivity::class.java))
            } else {
                startActivity(Intent(this, ShoppingListsActivity::class.java))
            }
            finish()
        }
    }
}