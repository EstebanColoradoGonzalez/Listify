package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.estebancoloradogonzalez.listify.databinding.FragmentCreateCategoryBinding
import com.estebancoloradogonzalez.listify.viewmodel.CategoryViewModel

class CreateCategoryFragment : Fragment() {

    private var _binding: FragmentCreateCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSaveButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSaveButton() {
        binding.btnSaveCategory.setOnClickListener { handleSaveCategory() }
    }

    private fun handleSaveCategory() {
        val categoryName = binding.etCategoryName.text.toString()
        viewModel.addCategory(
            categoryName,
            ::showCategoryNameError,
            ::onCategoryAdded
        )
    }

    private fun showCategoryNameError(errorMessage: String) {
        binding.tvCategoryNameError.text = errorMessage
        binding.tvCategoryNameError.visibility = View.VISIBLE
    }

    private fun onCategoryAdded() {
        binding.tvCategoryNameError.visibility = View.GONE
        findNavController().popBackStack()
    }
}
