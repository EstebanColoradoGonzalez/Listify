package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.estebancoloradogonzalez.listify.databinding.FragmentUpdateCategoryBinding
import com.estebancoloradogonzalez.listify.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateCategoryFragment : Fragment() {

    private var _binding: FragmentUpdateCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: UpdateCategoryFragmentArgs by navArgs()
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = args.categoryId

        CoroutineScope(Dispatchers.Main).launch {
            val category = withContext(Dispatchers.IO) {
                categoryViewModel.getCategoryById(categoryId)
            }
            category?.let {
                binding.etCategoryName.setText(it.name)
            }
        }

        binding.btnUpdateCategory.setOnClickListener {
            val newCategoryName = binding.etCategoryName.text.toString()
            categoryViewModel.updateCategory(categoryId, newCategoryName,
                onError = { errorMessage ->
                    binding.tvCategoryNameError.text = errorMessage
                    binding.tvCategoryNameError.visibility = View.VISIBLE
                },
                onSuccess = {
                    binding.tvCategoryNameError.visibility = View.GONE
                    findNavController().popBackStack()
                })
        }

        binding.btnDeleteCategory.setOnClickListener {
            categoryViewModel.deleteCategory(categoryId,
                onSuccess = {
                    findNavController().popBackStack()
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
