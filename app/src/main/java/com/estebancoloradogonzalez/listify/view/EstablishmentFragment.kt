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
import androidx.recyclerview.widget.LinearLayoutManager
import com.estebancoloradogonzalez.listify.databinding.FragmentEstablishmentBinding
import com.estebancoloradogonzalez.listify.view.adapter.ProductEstablishmentAdapter
import com.estebancoloradogonzalez.listify.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

class EstablishmentFragment : Fragment() {
    private var _binding: FragmentEstablishmentBinding? = null
    private val binding get() = _binding!!
    private val args: EstablishmentFragmentArgs by navArgs()
    private val shoppingListViewModel: ShoppingListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEstablishmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shoppingListId = args.shoppingListId
        val establishmentName = args.establishmentName

        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val products = shoppingListViewModel
                .getProductsByShoppingListAndEstablishment(shoppingListId, establishmentName)
            val adapter = ProductEstablishmentAdapter(products) { product ->
                val action = EstablishmentFragmentDirections
                    .actionEstablishmentFragmentToProductFragment(product.productShoppingListId)
                findNavController().navigate(action)
            }
            binding.rvProducts.adapter = adapter
        }

        binding.fabAddProduct.setOnClickListener {
            val action = EstablishmentFragmentDirections
                .actionEstablishmentFragmentToSelectProductFragment(shoppingListId)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
