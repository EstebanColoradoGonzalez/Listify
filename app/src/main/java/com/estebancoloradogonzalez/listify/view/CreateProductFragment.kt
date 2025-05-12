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
import kotlinx.coroutines.CoroutineScope
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

        val userId = args.userId

        loadData()

        binding.btnCreateProduct.setOnClickListener {
            val productName = binding.etProductName.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()

            val selectedUnitOfMeasurement = binding.spinnerUnitOfMeasurement.selectedItem.toString()
            val selectedPurchaseFrequency = binding.spinnerPurchaseFrequency.selectedItem.toString()
            val selectedEstablishment = binding.spinnerEstablishment.selectedItem.toString()
            val selectedCategory = binding.spinnerCategory.selectedItem.toString()

            lifecycleScope.launch {
                productViewModel.registerProduct(
                    productName,
                    productPrice,
                    productQuantity,
                    selectedUnitOfMeasurement,
                    selectedPurchaseFrequency,
                    selectedEstablishment,
                    selectedCategory,
                    userId,
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

    private fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            categories = withContext(Dispatchers.IO) { categoryViewModel.getCategories() }
            establishments = withContext(Dispatchers.IO) { establishmentViewModel.getEstablishments() }
            purchaseFrequencies = withContext(Dispatchers.IO) { purchaseFrequencyViewModel.getEPurchaseFrequencies() }
            unitsOfMeasurement = withContext(Dispatchers.IO) { unitOfMeasurementViewModel.getUnitsOfMeasurement() }

            setupSpinners()
        }
    }

    private fun setupSpinners() {
        val categoryNames = categories.map { it.name }
        val establishmentNames = establishments.map { it.name }
        val purchaseFrequencyNames = purchaseFrequencies.map { it.name }
        val unitOfMeasurementNames = unitsOfMeasurement.map { it.name }

        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryNames)
        val establishmentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, establishmentNames)
        val purchaseFrequencyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, purchaseFrequencyNames)
        val unitOfMeasurementAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, unitOfMeasurementNames)

        binding.spinnerCategory.adapter = categoryAdapter
        binding.spinnerEstablishment.adapter = establishmentAdapter
        binding.spinnerPurchaseFrequency.adapter = purchaseFrequencyAdapter
        binding.spinnerUnitOfMeasurement.adapter = unitOfMeasurementAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
