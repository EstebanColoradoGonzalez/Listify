package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.estebancoloradogonzalez.listify.databinding.FragmentSettingsBinding
import com.estebancoloradogonzalez.listify.utils.Messages
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.viewmodel.BudgetViewModel
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()
    private val budgetViewModel: BudgetViewModel by viewModels()

    private var userId: Long = NumericConstants.LONG_NEGATIVE_ONE
    private var originalName: String = TextConstants.EMPTY
    private var originalBudget: String = TextConstants.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserId()
        observeUserData()
        loadBudget()
        setupInputListeners()
        setupUpdateButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadUserId() {
        lifecycleScope.launch {
            userId = userViewModel.fetchUserId()
        }
    }

    private fun observeUserData() {
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                originalName = it.name
                binding.etFullName.setText(originalName)
            }
        }
    }

    private fun loadBudget() {
        lifecycleScope.launch {
            budgetViewModel.fetchBudget()?.let {
                originalBudget = it.value.toString()
                binding.etBudget.setText(originalBudget)
            }
        }
    }

    private fun setupInputListeners() {
        binding.etFullName.addTextChangedListener { updateButtonState() }
        binding.etBudget.addTextChangedListener { updateButtonState() }
    }

    private fun updateButtonState() {
        val nameChanged = binding.etFullName.text.toString() != originalName
        val budgetChanged = binding.etBudget.text.toString() != originalBudget
        binding.btnUpdate.isEnabled = nameChanged || budgetChanged
    }

    private fun setupUpdateButton() {
        binding.btnUpdate.setOnClickListener { updateUserAndBudget() }
    }

    private fun updateUserAndBudget() {
        val newName = binding.etFullName.text.toString().trim()
        val newBudget = binding.etBudget.text.toString().trim()

        userViewModel.updateUserName(userId, newName) { errorMessage ->
            binding.etFullName.error = errorMessage
            return@updateUserName
        }

        budgetViewModel.updateBudgetAmount(userId, newBudget) { errorMessage ->
            binding.etBudget.error = errorMessage
            return@updateBudgetAmount
        }

        Toast.makeText(requireContext(), Messages.DATA_UPDATED_SUCCESSFULLY_MESSAGE, Toast.LENGTH_SHORT).show()
        originalName = newName
        originalBudget = newBudget
        binding.btnUpdate.isEnabled = false
    }
}
