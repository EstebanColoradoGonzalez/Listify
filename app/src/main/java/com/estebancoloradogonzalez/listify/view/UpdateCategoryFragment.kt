package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.estebancoloradogonzalez.listify.databinding.FragmentUpdateCategoryBinding
import com.estebancoloradogonzalez.listify.viewmodel.CategoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateCategoryFragment : Fragment() {

    private var _binding: FragmentUpdateCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: UpdateCategoryFragmentArgs by navArgs()
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCategory()
        setupUpdateButton()
        setupDeleteButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadCategory() {
        val categoryId = args.categoryId
        viewLifecycleOwner.lifecycleScope.launch {
            val category = withContext(Dispatchers.IO) { viewModel.fetchCategoryById(categoryId) }
            category?.let { binding.etCategoryName.setText(it.name) }
        }
    }

    private fun setupUpdateButton() {
        binding.btnUpdateCategory.setOnClickListener { handleUpdateCategory() }
    }

    private fun handleUpdateCategory() {
        val categoryId = args.categoryId
        val newCategoryName = binding.etCategoryName.text.toString()
        viewModel.modifyCategory(
            categoryId,
            newCategoryName,
            onError = ::showError,
            onSuccess = ::onSuccess
        )
    }

    private fun showError(errorMessage: String) {
        binding.tvCategoryNameError.text = errorMessage
        binding.tvCategoryNameError.visibility = View.VISIBLE
    }

    private fun onSuccess() {
        binding.tvCategoryNameError.visibility = View.GONE
        findNavController().popBackStack()
    }

    private fun setupDeleteButton() {
        binding.btnDeleteCategory.setOnClickListener { handleDeleteCategory() }
    }

    private fun handleDeleteCategory() {
        val categoryId = args.categoryId
        viewModel.removeCategory(
            categoryId,
            onSuccess = { findNavController().popBackStack() }
        )
    }
}