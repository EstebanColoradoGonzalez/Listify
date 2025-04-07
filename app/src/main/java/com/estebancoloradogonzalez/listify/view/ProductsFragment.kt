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
import com.estebancoloradogonzalez.listify.databinding.FragmentProductsBinding
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.view.adapter.ProductAdapter
import com.estebancoloradogonzalez.listify.viewmodel.ProductViewModel
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            userId = userViewModel.getUserId()
            setupRecyclerView(userId)
        }

        binding.fabAddProduct.setOnClickListener {
            val action = ProductsFragmentDirections.actionProductsFragmentToCreateProductFragment(userId)
            findNavController().navigate(action)
        }

        binding.btnCategories.setOnClickListener {
            val action = ProductsFragmentDirections.actionProductsFragmentToCategoriesFragment()
            findNavController().navigate(action)
        }

        lifecycleScope.launch {
            val totalExpenditure = productViewModel.getTotalExpenditure() ?: 0.0
            binding.tvTotalExpenditure.text = TextConstants.TOTAL_EXPENDITURE + totalExpenditure
        }
    }

    private fun setupRecyclerView(user: Long) {
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val products = productViewModel.getProducts(user)
            val adapter = ProductAdapter(products) { product ->
                val action = ProductsFragmentDirections.actionProductsFragmentToUpdateProductFragment(product.id)
                findNavController().navigate(action)
            }
            binding.rvProducts.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
