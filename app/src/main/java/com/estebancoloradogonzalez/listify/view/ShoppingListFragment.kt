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
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.databinding.FragmentShoppingListBinding
import com.estebancoloradogonzalez.listify.model.dto.EstablishmentNameDTO
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
    private val viewModel: ShoppingListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEstablishmentRecycler()
        setupShoppingListDetails()
        setupCompleteButton()
        setupCancelButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupEstablishmentRecycler() {
        binding.rvEstablishments.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val establishments = viewModel.fetchEstablishmentsByShoppingList(args.shoppingListId)
            binding.rvEstablishments.adapter = createEstablishmentAdapter(establishments)
        }
    }

    private fun createEstablishmentAdapter(establishments: List<EstablishmentNameDTO>): EstablishmentAdapter {
        return EstablishmentAdapter(establishments) { establishment ->
            val action = ShoppingListFragmentDirections
                .actionShoppingListFragmentToEstablishmentFragment(
                    establishmentName = establishment.name,
                    shoppingListId = args.shoppingListId
                )
            findNavController().navigate(action)
        }
    }

    private fun setupShoppingListDetails() {
        lifecycleScope.launch {
            val shoppingList = viewModel.fetchShoppingListById(args.shoppingListId)
            val isActive = shoppingList?.status == TextConstants.STATUS_ACTIVE
            binding.btnCompleteShoppingList.isEnabled = isActive
            binding.btnCancelShoppingList.isEnabled = isActive

            val info = viewModel.fetchShoppingListDateAndTotalAmount(args.shoppingListId)
            info?.let {
                val formatter = DateTimeFormatter.ofPattern(TextConstants.DATE_FORMAT, Locale.getDefault())
                binding.tvShoppingListDate.text = it.shoppingListDate.format(formatter)
                val formattedAmount = String.format(Locale.getDefault(), TextConstants.AMOUNT_FORMAT, it.totalAmount)
                binding.tvShoppingListTotal.text = context?.getString(R.string.total_expenditure, formattedAmount)
            }
        }
    }

    private fun setupCompleteButton() {
        binding.btnCompleteShoppingList.setOnClickListener {
            handleShoppingListStatusChange(TextConstants.STATUS_COMPLETED)
        }
    }

    private fun setupCancelButton() {
        binding.btnCancelShoppingList.setOnClickListener {
            handleShoppingListStatusChange(TextConstants.STATUS_CANCELLED)
        }
    }

    private fun handleShoppingListStatusChange(status: String) {
        lifecycleScope.launch {
            viewModel.completeOrCancelShoppingList(
                args.userId,
                args.shoppingListId,
                status,
                ::showErrorMessage,
                ::onStatusChanged
            )
        }
    }

    private fun showErrorMessage(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = View.VISIBLE
    }

    private fun onStatusChanged() {
        binding.tvErrorMessage.visibility = View.GONE
        findNavController().popBackStack()
    }
}
