package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.estebancoloradogonzalez.listify.databinding.FragmentUserBudgetBinding
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel

class UserBudgetFragment : Fragment() {

    private var _binding: FragmentUserBudgetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()
    private val args: UserBudgetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStartButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupStartButton() {
        binding.btnStart.setOnClickListener { handleStart() }
    }

    private fun handleStart() {
        val userName = args.userName
        val budgetValue = binding.etBudget.text.toString()
        viewModel.registerUser(
            userName,
            budgetValue,
            ::showBudgetError,
            ::onUserRegistered
        )
    }

    private fun showBudgetError(errorMessage: String) {
        binding.tvBudgetError.text = errorMessage
        binding.tvBudgetError.visibility = View.VISIBLE
    }

    private fun onUserRegistered(userId: Long) {
        binding.tvBudgetError.visibility = View.GONE
        val action = UserBudgetFragmentDirections.actionUserBudgetFragmentToShoppingListsFragment(userId)
        findNavController().navigate(action)
    }
}
