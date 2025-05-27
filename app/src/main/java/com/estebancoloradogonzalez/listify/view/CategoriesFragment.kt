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
import com.estebancoloradogonzalez.listify.databinding.FragmentCategoriesBinding
import com.estebancoloradogonzalez.listify.model.entity.Category
import com.estebancoloradogonzalez.listify.view.adapter.CategoryAdapter
import com.estebancoloradogonzalez.listify.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFab()
        setupCategoryList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupFab() {
        binding.fabAddCategory.setOnClickListener { navigateToCreateCategory() }
    }

    private fun navigateToCreateCategory() {
        val action = CategoriesFragmentDirections.actionCategoriesFragmentToCreateCategoryFragment()
        findNavController().navigate(action)
    }

    private fun setupCategoryList() {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        loadCategories()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories = viewModel.fetchCategories()
            binding.rvCategories.adapter = createCategoryAdapter(categories)
        }
    }

    private fun createCategoryAdapter(categories: List<Category>): CategoryAdapter {
        return CategoryAdapter(categories) { category ->
            navigateToUpdateCategory(category.id)
        }
    }

    private fun navigateToUpdateCategory(categoryId: Long) {
        val action = CategoriesFragmentDirections.actionCategoriesFragmentToUpdateCategoryFragment(categoryId)
        findNavController().navigate(action)
    }
}
