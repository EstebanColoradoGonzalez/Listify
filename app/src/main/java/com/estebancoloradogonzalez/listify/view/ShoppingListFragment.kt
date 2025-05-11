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
import com.estebancoloradogonzalez.listify.databinding.FragmentShoppingListBinding
import com.estebancoloradogonzalez.listify.view.adapter.EstablishmentAdapter
import com.estebancoloradogonzalez.listify.viewmodel.ShoppingListViewModel
import com.estebancoloradogonzalez.listify.utils.TextConstants
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

class ShoppingListFragment : Fragment() {
    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!
    private val args: ShoppingListFragmentArgs by navArgs()
    private val shoppingListViewModel: ShoppingListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shoppingListId = args.shoppingListId
        val userId = args.userId

        lifecycleScope.launch {
            val info = shoppingListViewModel.getShoppingListDateAndTotalAmount(shoppingListId)
            info?.let {
                val formatter = DateTimeFormatter.ofPattern(TextConstants.DATE_FORMAT, Locale.getDefault())
                binding.tvShoppingListDate.text = it.shoppingListDate.format(formatter)
                binding.tvShoppingListTotal.text = TextConstants.TOTAL_EXPENDITURE + String.format(TextConstants.AMOUNT_FORMAT, it.totalAmount)
            }
        }

        binding.btnDeleteShoppingList.setOnClickListener {
            shoppingListViewModel.deleteShoppingList(shoppingListId) {
                findNavController().popBackStack()
            }
        }

        binding.btnCompleteShoppingList.setOnClickListener {
            shoppingListViewModel.completeOrCancelShoppingList(
                userId,
                shoppingListId,
                TextConstants.STATUS_COMPLETED
            ) {
                findNavController().popBackStack()
            }
        }

        binding.btnCancelShoppingList.setOnClickListener {
            shoppingListViewModel.completeOrCancelShoppingList(
                userId,
                shoppingListId,
                TextConstants.STATUS_CANCELLED
            ) {
                findNavController().popBackStack()
            }
        }

        binding.rvEstablishments.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val establishments = shoppingListViewModel.getEstablishmentFromAShoppingList(shoppingListId)
            val adapter = EstablishmentAdapter(establishments) { establishment ->
                val action = ShoppingListFragmentDirections
                    .actionShoppingListFragmentToEstablishmentFragment(
                        establishmentName = establishment.name,
                        shoppingListId = shoppingListId
                    )
                findNavController().navigate(action)
            }
            binding.rvEstablishments.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
