package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.estebancoloradogonzalez.listify.databinding.FragmentUpdateProductBinding
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.viewmodel.EstablishmentViewModel
import com.estebancoloradogonzalez.listify.viewmodel.ProductViewModel
import com.estebancoloradogonzalez.listify.viewmodel.CategoryViewModel
import com.estebancoloradogonzalez.listify.viewmodel.PurchaseFrequencyViewModel
import com.estebancoloradogonzalez.listify.viewmodel.UnitOfMeasurementViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateProductFragment : Fragment() {

    private var _binding: FragmentUpdateProductBinding? = null
    private val binding get() = _binding!!
    private val args: UpdateProductFragmentArgs by navArgs()

    private val productViewModel: ProductViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val establishmentViewModel: EstablishmentViewModel by viewModels()
    private val purchaseFrequencyViewModel: PurchaseFrequencyViewModel by viewModels()
    private val unitOfMeasurementViewModel: UnitOfMeasurementViewModel by viewModels()

    private var categories: List<String> = emptyList()
    private var establishments: List<String> = emptyList()
    private var purchaseFrequencies: List<String> = emptyList()
    private var unitsOfMeasurement: List<String> = emptyList()
    private val statusOptions = listOf(TextConstants.PRODUCT_STATUS_ACTIVE, TextConstants.PRODUCT_STATUS_DESACTIVE)

    private var productToUpdate: com.estebancoloradogonzalez.listify.model.dto.ProductToUpdateDTO? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusSpinner()
        loadData()
        setupUpdateButton()
        setupDeleteButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupStatusSpinner() {
        val statusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusOptions)
        binding.spinnerIsActive.adapter = statusAdapter
    }

    private fun loadData() {
        val productId = args.productId
        viewLifecycleOwner.lifecycleScope.launch {
            categories = withContext(Dispatchers.IO) { categoryViewModel.fetchCategories().map { it.name } }
            establishments = withContext(Dispatchers.IO) { establishmentViewModel.fetchEstablishments().map { it.name } }
            purchaseFrequencies = withContext(Dispatchers.IO) { purchaseFrequencyViewModel.fetchPurchaseFrequencies().map { it.name } }
            unitsOfMeasurement = withContext(Dispatchers.IO) { unitOfMeasurementViewModel.fetchUnitsOfMeasurement().map { it.name } }
            productToUpdate = withContext(Dispatchers.IO) { productViewModel.fetchProductToUpdate(productId) }
            setupSpinnersAndInputs()
        }
    }

    private fun setupSpinnersAndInputs() {
        productToUpdate?.let { product ->
            binding.etProductName.setText(product.name)
            binding.etProductPrice.setText(product.unitPrice.toString())
            binding.etProductQuantity.setText(product.amount.toString())

            setSpinner(binding.spinnerUnitOfMeasurement, unitsOfMeasurement, product.unitOfMeasurement)
            setSpinner(binding.spinnerPurchaseFrequency, purchaseFrequencies, product.purchaseFrequency)
            setSpinner(binding.spinnerEstablishment, establishments, product.establishment)
            setSpinner(binding.spinnerCategory, categories, product.category)

            val status = if (product.isActive) TextConstants.PRODUCT_STATUS_ACTIVE else TextConstants.PRODUCT_STATUS_DESACTIVE
            setSpinner(binding.spinnerIsActive, statusOptions, status)
        }
    }

    private fun setSpinner(spinner: android.widget.Spinner, items: List<String>, selected: String?) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapter
        val pos = items.indexOf(selected)
        if (pos >= 0) spinner.setSelection(pos)
    }

    private fun setupUpdateButton() {
        binding.btnUpdateProduct.setOnClickListener { handleUpdateProduct() }
    }

    private fun handleUpdateProduct() {
        val productId = args.productId
        val productName = binding.etProductName.text.toString()
        val productPrice = binding.etProductPrice.text.toString()
        val productQuantity = binding.etProductQuantity.text.toString()
        val selectedUnitOfMeasurement = binding.spinnerUnitOfMeasurement.selectedItem.toString()
        val selectedPurchaseFrequency = binding.spinnerPurchaseFrequency.selectedItem.toString()
        val selectedEstablishment = binding.spinnerEstablishment.selectedItem.toString()
        val selectedCategory = binding.spinnerCategory.selectedItem.toString()
        val isActive = binding.spinnerIsActive.selectedItem.toString() == TextConstants.PRODUCT_STATUS_ACTIVE

        viewLifecycleOwner.lifecycleScope.launch {
            val product = productViewModel.fetchProductById(productId)
            if (product != null) {
                productViewModel.updateProduct(
                    productName,
                    productPrice,
                    productQuantity,
                    selectedUnitOfMeasurement,
                    selectedPurchaseFrequency,
                    selectedEstablishment,
                    selectedCategory,
                    isActive,
                    productId,
                    product.user,
                    ::showError,
                    ::onProductUpdated
                )
            }
        }
    }

    private fun showError(errorMessage: String) {
        binding.tvError.text = errorMessage
        binding.tvError.visibility = View.VISIBLE
    }

    private fun onProductUpdated() {
        binding.tvError.visibility = View.GONE
        findNavController().popBackStack()
    }

    private fun setupDeleteButton() {
        binding.btnDeleteProduct.setOnClickListener { handleDeleteProduct() }
    }

    private fun handleDeleteProduct() {
        val productId = args.productId
        productViewModel.deleteProduct(
            productId,
            onSuccess = { findNavController().popBackStack() }
        )
    }
}