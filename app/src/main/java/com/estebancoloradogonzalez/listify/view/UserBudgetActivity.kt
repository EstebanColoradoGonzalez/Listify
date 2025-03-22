package com.estebancoloradogonzalez.listify.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.estebancoloradogonzalez.listify.databinding.ActivityUserBudgetBinding
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.viewmodel.BudgetViewModel

class UserBudgetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBudgetBinding
    private val budgetViewModel: BudgetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getLongExtra(TextConstants.PARAM_USER_ID, NumericConstants.LONG_NEGATIVE_ONE)

        binding.btnStart.setOnClickListener {
            val budgetValue = binding.etBudget.text.toString()
            budgetViewModel.insertBudget(budgetValue, userId) { errorMessage ->
                binding.tvBudgetError.text = errorMessage
                binding.tvBudgetError.visibility = View.VISIBLE
            }

            val intent = Intent(this, ShoppingListsActivity::class.java).apply {
                putExtra(TextConstants.PARAM_USER_ID, userId)
            }
            
            startActivity(intent)
            finish()
        }
    }
}
