package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.databinding.FragmentShoppingListsBinding
import com.estebancoloradogonzalez.listify.viewmodel.UserViewModel
import androidx.lifecycle.lifecycleScope
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import kotlinx.coroutines.launch

class ShoppingListsFragment : Fragment() {
    private val args: ShoppingListsFragmentArgs by navArgs()
    private var _binding: FragmentShoppingListsBinding? = null
    private val userViewModel: UserViewModel by viewModels()
    private var userId = NumericConstants.LONG_NEGATIVE_ONE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shopping_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.userId == -1L) {
            lifecycleScope.launch {
                userId = userViewModel.getUserId()
            }
        } else {
            userId = args.userId
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}