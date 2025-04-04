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
    private val userViewModel: UserViewModel by viewModels()
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

        val userName = args.userName

        binding.btnStart.setOnClickListener {
            val budgetValue = binding.etBudget.text.toString()

            userViewModel.registerUser(userName, budgetValue, { errorMessage ->
                binding.tvBudgetError.text = errorMessage
                binding.tvBudgetError.visibility = View.VISIBLE
            }) { userId ->
                binding.tvBudgetError.visibility = View.GONE
                val action = UserBudgetFragmentDirections.actionUserBudgetFragmentToShoppingListsFragment(userId)
                findNavController().navigate(action)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
