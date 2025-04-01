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
import com.estebancoloradogonzalez.listify.view.adapter.CategoryAdapter
import com.estebancoloradogonzalez.listify.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddCategory.setOnClickListener {
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToCreateCategoryFragment()
            findNavController().navigate(action)
        }

        setupRecyclerView()
        loadCategories()
    }

    private fun setupRecyclerView() {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories = categoryViewModel.getCategories()
            val adapter = CategoryAdapter(categories) { category ->
                val action = CategoriesFragmentDirections.actionCategoriesFragmentToUpdateCategoryFragment(category.id)
                findNavController().navigate(action)
            }
            binding.rvCategories.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
