package com.estebancoloradogonzalez.listify.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.estebancoloradogonzalez.listify.databinding.ActivityRegisterBinding
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val name = binding.etUserName.text.toString()
            if (name.isNotEmpty()) {
                userViewModel.registerUser(name) { userId ->
                    val intent = Intent(this, UserBudgetActivity::class.java).apply {
                        putExtra(TextConstants.PARAM_USER_ID, userId)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}