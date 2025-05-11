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
    private val productShoppingListViewModel: ProductShoppingListViewModel by viewModels()

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
        val shoppingListId = args.shoppingListId
        val establishmentName = args.establishmentName

        binding.btnSelectProduct.isEnabled = false

        lifecycleScope.launch {
            productsToSelect = productShoppingListViewModel.getProductsToSelect(shoppingListId, establishmentName)
            if (productsToSelect.isEmpty()) {
                binding.actvProducts.visibility = View.GONE
                binding.btnSelectProduct.visibility = View.GONE
                binding.tvNoProducts.visibility = View.VISIBLE
            } else {
                binding.actvProducts.visibility = View.VISIBLE
                binding.btnSelectProduct.visibility = View.VISIBLE
                binding.tvNoProducts.visibility = View.GONE

                val productNames = productsToSelect.map { it.name }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, productNames)
                binding.actvProducts.setAdapter(adapter)

                binding.actvProducts.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        binding.actvProducts.showDropDown()
                    } else {
                        val input = binding.actvProducts.text.toString()
                        val match = productsToSelect.find { it.name == input }
                        selectedProductId = match?.id
                        binding.btnSelectProduct.isEnabled = (match != null)
                    }
                }
                binding.actvProducts.setOnClickListener {
                    binding.actvProducts.showDropDown()
                }

                binding.actvProducts.setOnItemClickListener { _, _, position, _ ->
                    selectedProductId = productsToSelect[position].id
                    binding.btnSelectProduct.isEnabled = true
                }

                binding.actvProducts.addTextChangedListener {
                    val input = binding.actvProducts.text.toString()
                    val match = productsToSelect.find { it.name == input }
                    selectedProductId = match?.id
                    binding.btnSelectProduct.isEnabled = (match != null)
                }

                binding.btnSelectProduct.setOnClickListener {
                    val input = binding.actvProducts.text.toString()
                    val product = productsToSelect.find { it.name == input }
                    if (product != null) {
                        productShoppingListViewModel.selectProduct(
                            product.id,
                            shoppingListId
                        ) {
                            findNavController().popBackStack()
                        }
                    } else {
                        binding.actvProducts.error = getString(com.estebancoloradogonzalez.listify.R.string.select_product)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

