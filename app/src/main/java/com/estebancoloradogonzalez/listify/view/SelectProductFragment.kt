package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.estebancoloradogonzalez.listify.databinding.FragmentSelectProductBinding
import com.estebancoloradogonzalez.listify.model.dto.ProductIdNameDTO
import com.estebancoloradogonzalez.listify.viewmodel.ProductShoppingListViewModel
import kotlinx.coroutines.launch

class SelectProductFragment : Fragment() {

    private var _binding: FragmentSelectProductBinding? = null
    private val binding get() = _binding!!
    private val args: SelectProductFragmentArgs by navArgs()
    private val viewModel: ProductShoppingListViewModel by viewModels()

    private var productsToSelect: List<ProductIdNameDTO> = emptyList()
    private var selectedProductId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAvailableProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadAvailableProducts() {
        val shoppingListId = args.shoppingListId
        val establishmentName = args.establishmentName

        binding.btnSelectProduct.isEnabled = false

        lifecycleScope.launch {
            productsToSelect = viewModel.fetchAvailableProductsForSelection(shoppingListId, establishmentName)
            if (productsToSelect.isEmpty()) {
                showNoProductsMessage()
            } else {
                setupProductAutoComplete(productsToSelect)
                setupSelectProductButton(shoppingListId)
            }
        }
    }

    private fun showNoProductsMessage() {
        binding.actvProducts.visibility = View.GONE
        binding.btnSelectProduct.visibility = View.GONE
        binding.tvNoProducts.visibility = View.VISIBLE
    }

    private fun setupProductAutoComplete(products: List<ProductIdNameDTO>) {
        binding.actvProducts.visibility = View.VISIBLE
        binding.btnSelectProduct.visibility = View.VISIBLE
        binding.tvNoProducts.visibility = View.GONE

        val productNames = products.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, productNames)
        binding.actvProducts.setAdapter(adapter)

        binding.actvProducts.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.actvProducts.showDropDown()
            } else {
                updateSelectedProductId()
            }
        }
        binding.actvProducts.setOnClickListener { binding.actvProducts.showDropDown() }
        binding.actvProducts.setOnItemClickListener { _, _, position, _ ->
            selectedProductId = products[position].id
            binding.btnSelectProduct.isEnabled = true
        }
        binding.actvProducts.addTextChangedListener { updateSelectedProductId() }
    }

    private fun updateSelectedProductId() {
        val input = binding.actvProducts.text.toString()
        val match = productsToSelect.find { it.name == input }
        selectedProductId = match?.id
        binding.btnSelectProduct.isEnabled = (match != null)
    }

    private fun setupSelectProductButton(shoppingListId: Long) {
        binding.btnSelectProduct.setOnClickListener {
            val input = binding.actvProducts.text.toString()
            val product = productsToSelect.find { it.name == input }
            if (product != null) {
                viewModel.addProductToShoppingList(product.id, shoppingListId) {
                    findNavController().popBackStack()
                }
            } else {
                binding.actvProducts.error = getString(com.estebancoloradogonzalez.listify.R.string.select_product)
            }
        }
    }
}
