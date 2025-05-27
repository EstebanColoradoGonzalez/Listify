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
import androidx.recyclerview.widget.LinearLayoutManager
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.databinding.FragmentEstablishmentBinding
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import com.estebancoloradogonzalez.listify.view.adapter.ProductEstablishmentAdapter
import com.estebancoloradogonzalez.listify.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class EstablishmentFragment : Fragment() {

    private var _binding: FragmentEstablishmentBinding? = null
    private val binding get() = _binding!!
    private val args: EstablishmentFragmentArgs by navArgs()
    private val viewModel: ShoppingListViewModel by viewModels()

    private lateinit var productAdapter: ProductEstablishmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstablishmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        setupRecyclerView()
        setupFab()
        loadEstablishmentData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = args.establishmentName
    }

    private fun setupRecyclerView() {
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupFab() {
        binding.fabAddProduct.setOnClickListener {
            navigateToSelectProduct()
        }
    }

    private fun loadEstablishmentData() {
        lifecycleScope.launch {
            val shoppingListId = args.shoppingListId
            val establishmentName = args.establishmentName

            val shoppingList = viewModel.fetchShoppingListById(shoppingListId)
            val isActive = shoppingList?.status == TextConstants.STATUS_ACTIVE

            productAdapter = createProductAdapter(isActive)
            binding.rvProducts.adapter = productAdapter
            binding.fabAddProduct.isEnabled = isActive

            reloadProducts(shoppingListId, establishmentName)
            reloadTotal(shoppingListId, establishmentName)
        }
    }

    private fun createProductAdapter(isActive: Boolean): ProductEstablishmentAdapter {
        return ProductEstablishmentAdapter(
            products = listOf(),
            onItemClick = { product -> navigateToProduct(product.productShoppingListId, product.productName) },
            onReadyChange = { productId, isReady -> handleProductReadyChange(productId, isReady) },
            isActive = isActive
        )
    }

    private fun navigateToProduct(productId: Long, productName: String) {
        val action = EstablishmentFragmentDirections
            .actionEstablishmentFragmentToProductFragment(productId, productName)
        findNavController().navigate(action)
    }

    private fun handleProductReadyChange(productId: Long, isReady: Boolean) {
        lifecycleScope.launch {
            viewModel.updateProductIsReady(productId, isReady)
            reloadProducts(args.shoppingListId, args.establishmentName)
            reloadTotal(args.shoppingListId, args.establishmentName)
        }
    }

    private fun navigateToSelectProduct() {
        val action = EstablishmentFragmentDirections
            .actionEstablishmentFragmentToSelectProductFragment(args.shoppingListId, args.establishmentName)
        findNavController().navigate(action)
    }

    private suspend fun reloadProducts(shoppingListId: Long, establishmentName: String) {
        val products = viewModel
            .fetchProductsByShoppingListAndEstablishment(shoppingListId, establishmentName)
            .sortedBy { it.isReady }
        productAdapter.updateProducts(products)
    }

    private suspend fun reloadTotal(shoppingListId: Long, establishmentName: String) {
        val total = viewModel.fetchTotalAmountByShoppingListAndEstablishment(
            shoppingListId, establishmentName
        ) ?: NumericConstants.ZERO_POINT_ZERO
        val formattedAmount = String.format(Locale.getDefault(), TextConstants.AMOUNT_FORMAT, total)
        binding.tvTotalAmount.text = context?.getString(R.string.total_expenditure, formattedAmount)
    }
}
