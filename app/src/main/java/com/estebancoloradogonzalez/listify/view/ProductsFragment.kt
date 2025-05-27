package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.databinding.FragmentProductsBinding
import com.estebancoloradogonzalez.listify.model.dto.ProductDTO
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.view.adapter.ProductAdapter
import com.estebancoloradogonzalez.listify.viewmodel.ProductViewModel
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private var userId = NumericConstants.LONG_NEGATIVE_ONE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchUserIdAndSetup()
        setupFab()
        setupCategoriesButton()
        displayTotalExpenditure()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchUserIdAndSetup() {
        lifecycleScope.launch {
            userId = userViewModel.fetchUserId()
            setupRecyclerView(userId)
        }
    }

    private fun setupRecyclerView(user: Long) {
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val products = productViewModel.fetchProducts(user)
            binding.rvProducts.adapter = createProductAdapter(products)
        }
    }

    private fun createProductAdapter(products: List<ProductDTO>): ProductAdapter {
        return ProductAdapter(products) { product ->
            val action = ProductsFragmentDirections.actionProductsFragmentToUpdateProductFragment(product.id)
            findNavController().navigate(action)
        }
    }

    private fun setupFab() {
        binding.fabAddProduct.setOnClickListener {
            val action = ProductsFragmentDirections.actionProductsFragmentToCreateProductFragment(userId)
            findNavController().navigate(action)
        }
    }

    private fun setupCategoriesButton() {
        binding.btnCategories.setOnClickListener {
            val action = ProductsFragmentDirections.actionProductsFragmentToCategoriesFragment()
            findNavController().navigate(action)
        }
    }

    private fun displayTotalExpenditure() {
        lifecycleScope.launch {
            val totalExpenditure = productViewModel.fetchTotalExpenditure() ?: NumericConstants.ZERO_POINT_ZERO
            val formattedExpenditure = String.format(Locale.getDefault(), TextConstants.AMOUNT_FORMAT, totalExpenditure)
            binding.tvTotalExpenditure.text = context?.getString(R.string.total_expenditure, formattedExpenditure)
        }
    }
}