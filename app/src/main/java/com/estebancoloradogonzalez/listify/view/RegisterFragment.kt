package com.estebancoloradogonzalez.listify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.estebancoloradogonzalez.listify.databinding.FragmentRegisterBinding
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNextButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupNextButton() {
        binding.btnNext.setOnClickListener { handleNextClick() }
    }

    private fun handleNextClick() {
        val userName = binding.etUserName.text.toString()
        if (!InputValidator.isValidName(userName)) {
            showErrorMessage(Messages.ENTER_VALID_NAME_MESSAGE)
        } else {
            hideErrorMessage()
            navigateToUserBudget(userName)
        }
    }

    private fun showErrorMessage(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        binding.tvErrorMessage.visibility = View.GONE
    }

    private fun navigateToUserBudget(userName: String) {
        val action = RegisterFragmentDirections.actionRegisterFragmentToUserBudgetFragment(userName)
        findNavController().navigate(action)
    }
}