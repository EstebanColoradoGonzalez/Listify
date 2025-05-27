package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.estebancoloradogonzalez.listify.databinding.FragmentShoppingListsBinding
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListDTO
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.view.adapter.ShoppingListAdapter
import com.estebancoloradogonzalez.listify.viewmodel.ShoppingListViewModel
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class ShoppingListsFragment : Fragment() {

    private val args: ShoppingListsFragmentArgs by navArgs()
    private var _binding: FragmentShoppingListsBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    private val shoppingListViewModel: ShoppingListViewModel by viewModels()
    private var userId = NumericConstants.LONG_NEGATIVE_ONE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resolveUserIdAndSetup()
        setupFab()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun resolveUserIdAndSetup() {
        if (args.userId == NumericConstants.LONG_NEGATIVE_ONE) {
            lifecycleScope.launch {
                userId = userViewModel.fetchUserId()
                setupRecyclerView(userId)
            }
        } else {
            userId = args.userId
            setupRecyclerView(userId)
        }
    }

    private fun setupRecyclerView(user: Long) {
        binding.rvShoppingLists.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val shoppingLists: List<ShoppingListDTO> = shoppingListViewModel.fetchShoppingLists(user)
            val adapter = ShoppingListAdapter(shoppingLists) { shoppingList ->
                navigateToShoppingList(shoppingList.id)
            }
            binding.rvShoppingLists.adapter = adapter
        }
    }

    private fun setupFab() {
        binding.fabGenerateShoppingList.setOnClickListener {
            val action = ShoppingListsFragmentDirections
                .actionShoppingListsFragmentToGenerateShoppingListFragment(userId)
            findNavController().navigate(action)
        }
    }

    private fun navigateToShoppingList(shoppingListId: Long) {
        val action = ShoppingListsFragmentDirections
            .actionShoppingListsFragmentToShoppingListFragment(shoppingListId, userId)
        findNavController().navigate(action)
    }
}