package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.estebancoloradogonzalez.listify.databinding.FragmentProductBinding
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.viewmodel.ProductShoppingListViewModel
import kotlinx.coroutines.launch

class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val args: ProductFragmentArgs by navArgs()
    private val productShoppingListViewModel: ProductShoppingListViewModel by viewModels()

    private var initialPrice: String = TextConstants.EMPTY
    private var initialQuantity: String = TextConstants.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productShoppingListId = args.productShoppingListId
        val productName = args.productName

        (activity as? AppCompatActivity)?.supportActionBar?.title = productName

        binding.btnUpdateProduct.isEnabled = false

        lifecycleScope.launch {
            val detail = productShoppingListViewModel.getProductShoppingListDetailById(productShoppingListId)
            detail?.let { product ->
                initialPrice = product.unitPrice.toString()
                initialQuantity = product.purchasedAmount.toString()

                binding.etProductPrice.setText(initialPrice)
                binding.etProductQuantity.setText(initialQuantity)
                binding.tvUnitOfMeasurement.text = product.unitOfMeasurementName
            }

            setupInputListeners()
        }

        binding.btnUpdateProduct.setOnClickListener {
            val newPrice = binding.etProductPrice.text.toString().trim()
            val newQuantity = binding.etProductQuantity.text.toString().trim()

            lifecycleScope.launch {
                productShoppingListViewModel.updateProduct(productShoppingListId, newPrice, newQuantity,
                    { errorMessage ->
                        binding.tvError.text = errorMessage
                        binding.tvError.visibility = View.VISIBLE
                    }) {
                    binding.tvError.visibility = View.GONE
                    findNavController().popBackStack()
                }
            }
        }

        binding.btnDeleteProduct.setOnClickListener {
            productShoppingListViewModel.deleteProduct(productShoppingListId,
                onSuccess = {
                    findNavController().popBackStack()
                })
        }

    }

    private fun setupInputListeners() {
        val watcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIfInputsChanged()
            }
            override fun afterTextChanged(s: android.text.Editable?) { }
        }

        binding.etProductPrice.addTextChangedListener(watcher)
        binding.etProductQuantity.addTextChangedListener(watcher)
    }

    private fun checkIfInputsChanged() {
        val currentPrice = binding.etProductPrice.text.toString()
        val currentQuantity = binding.etProductQuantity.text.toString()

        binding.btnUpdateProduct.isEnabled = (currentPrice != initialPrice) || (currentQuantity != initialQuantity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

