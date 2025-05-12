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
import kotlinx.coroutines.CoroutineScope
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

    private var productToUpdateDTO: com.estebancoloradogonzalez.listify.model.dto.ProductToUpdateDTO? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = args.productId

        val statusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusOptions)
        binding.spinnerIsActive.adapter = statusAdapter

        loadData(productId)

        binding.btnUpdateProduct.setOnClickListener {
            val productName = binding.etProductName.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()
            val selectedUnitOfMeasurement = binding.spinnerUnitOfMeasurement.selectedItem.toString()
            val selectedPurchaseFrequency = binding.spinnerPurchaseFrequency.selectedItem.toString()
            val selectedEstablishment = binding.spinnerEstablishment.selectedItem.toString()
            val selectedCategory = binding.spinnerCategory.selectedItem.toString()
            val isActive = binding.spinnerIsActive.selectedItem.toString() == TextConstants.PRODUCT_STATUS_ACTIVE

            lifecycleScope.launch {
                val product = productViewModel.getProductById(productId)

                if(product != null) {
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
                        { errorMessage ->
                            binding.tvError.text = errorMessage
                            binding.tvError.visibility = View.VISIBLE
                        }) {
                        binding.tvError.visibility = View.GONE
                        findNavController().popBackStack()
                    }
                }
            }
        }

        binding.btnDeleteProduct.setOnClickListener {
            productViewModel.deleteProduct(productId,
                onSuccess = {
                    findNavController().popBackStack()
                })
        }
    }

    private fun loadData(productId: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            categories = withContext(Dispatchers.IO) { categoryViewModel.getCategories().map { it.name } }
            establishments = withContext(Dispatchers.IO) { establishmentViewModel.getEstablishments().map { it.name } }
            purchaseFrequencies = withContext(Dispatchers.IO) { purchaseFrequencyViewModel.getEPurchaseFrequencies().map { it.name } }
            unitsOfMeasurement = withContext(Dispatchers.IO) { unitOfMeasurementViewModel.getUnitsOfMeasurement().map { it.name } }

            productToUpdateDTO = withContext(Dispatchers.IO) { productViewModel.getProductToUpdate(productId) }

            setupSpinnersAndInputs()
        }
    }

    private fun setupSpinnersAndInputs() {
        productToUpdateDTO?.let { product ->
            binding.etProductName.setText(product.name)
            binding.etProductPrice.setText(product.unitPrice.toString())
            binding.etProductQuantity.setText(product.amount.toString())

            val unitAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, unitsOfMeasurement)
            binding.spinnerUnitOfMeasurement.adapter = unitAdapter
            val unitPos = unitsOfMeasurement.indexOf(product.unitOfMeasurement)
            if (unitPos >= 0) binding.spinnerUnitOfMeasurement.setSelection(unitPos)

            val frequencyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, purchaseFrequencies)
            binding.spinnerPurchaseFrequency.adapter = frequencyAdapter
            val frequencyPos = purchaseFrequencies.indexOf(product.purchaseFrequency)
            if (frequencyPos >= 0) binding.spinnerPurchaseFrequency.setSelection(frequencyPos)

            val establishmentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, establishments)
            binding.spinnerEstablishment.adapter = establishmentAdapter
            val establishmentPos = establishments.indexOf(product.establishment)
            if (establishmentPos >= 0) binding.spinnerEstablishment.setSelection(establishmentPos)

            val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
            binding.spinnerCategory.adapter = categoryAdapter
            val categoryPos = categories.indexOf(product.category)
            if (categoryPos >= 0) binding.spinnerCategory.setSelection(categoryPos)

            val selectedStatus = if (product.isActive) TextConstants.PRODUCT_STATUS_ACTIVE else TextConstants.PRODUCT_STATUS_DESACTIVE
            val statusPos = statusOptions.indexOf(selectedStatus)
            binding.spinnerIsActive.setSelection(statusPos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
