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
import com.estebancoloradogonzalez.listify.databinding.FragmentCreateProductBinding
import com.estebancoloradogonzalez.listify.model.entity.Category
import com.estebancoloradogonzalez.listify.model.entity.Establishment
import com.estebancoloradogonzalez.listify.model.entity.PurchaseFrequency
import com.estebancoloradogonzalez.listify.model.entity.UnitOfMeasurement
import com.estebancoloradogonzalez.listify.viewmodel.CategoryViewModel
import com.estebancoloradogonzalez.listify.viewmodel.EstablishmentViewModel
import com.estebancoloradogonzalez.listify.viewmodel.ProductViewModel
import com.estebancoloradogonzalez.listify.viewmodel.PurchaseFrequencyViewModel
import com.estebancoloradogonzalez.listify.viewmodel.UnitOfMeasurementViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateProductFragment : Fragment() {

    private var _binding: FragmentCreateProductBinding? = null
    private val binding get() = _binding!!
    private val args: CreateProductFragmentArgs by navArgs()

    private val productViewModel: ProductViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val establishmentViewModel: EstablishmentViewModel by viewModels()
    private val purchaseFrequencyViewModel: PurchaseFrequencyViewModel by viewModels()
    private val unitOfMeasurementViewModel: UnitOfMeasurementViewModel by viewModels()

    private var categories: List<Category> = emptyList()
    private var establishments: List<Establishment> = emptyList()
    private var purchaseFrequencies: List<PurchaseFrequency> = emptyList()
    private var unitsOfMeasurement: List<UnitOfMeasurement> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        setupCreateProductButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch {
            categories = withContext(Dispatchers.IO) { categoryViewModel.fetchCategories() }
            establishments = withContext(Dispatchers.IO) { establishmentViewModel.fetchEstablishments() }
            purchaseFrequencies = withContext(Dispatchers.IO) { purchaseFrequencyViewModel.fetchPurchaseFrequencies() }
            unitsOfMeasurement = withContext(Dispatchers.IO) { unitOfMeasurementViewModel.fetchUnitsOfMeasurement() }
            setupSpinners()
        }
    }

    private fun setupSpinners() {
        setSpinnerAdapter(binding.spinnerCategory, categories.map { it.name })
        setSpinnerAdapter(binding.spinnerEstablishment, establishments.map { it.name })
        setSpinnerAdapter(binding.spinnerPurchaseFrequency, purchaseFrequencies.map { it.name })
        setSpinnerAdapter(binding.spinnerUnitOfMeasurement, unitsOfMeasurement.map { it.name })
    }

    private fun setSpinnerAdapter(spinner: android.widget.Spinner, items: List<String>) {
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
    }

    private fun setupCreateProductButton() {
        binding.btnCreateProduct.setOnClickListener { handleCreateProduct() }
    }

    private fun handleCreateProduct() {
        val productName = binding.etProductName.text.toString()
        val productPrice = binding.etProductPrice.text.toString()
        val productQuantity = binding.etProductQuantity.text.toString()
        val selectedUnitOfMeasurement = binding.spinnerUnitOfMeasurement.selectedItem.toString()
        val selectedPurchaseFrequency = binding.spinnerPurchaseFrequency.selectedItem.toString()
        val selectedEstablishment = binding.spinnerEstablishment.selectedItem.toString()
        val selectedCategory = binding.spinnerCategory.selectedItem.toString()
        val userId = args.userId

        viewLifecycleOwner.lifecycleScope.launch {
            productViewModel.registerProduct(
                productName,
                productPrice,
                productQuantity,
                selectedUnitOfMeasurement,
                selectedPurchaseFrequency,
                selectedEstablishment,
                selectedCategory,
                userId,
                ::showError,
                ::onProductCreated
            )
        }
    }

    private fun showError(errorMessage: String) {
        binding.tvError.text = errorMessage
        binding.tvError.visibility = View.VISIBLE
    }

    private fun onProductCreated() {
        binding.tvError.visibility = View.GONE
        findNavController().popBackStack()
    }
}
