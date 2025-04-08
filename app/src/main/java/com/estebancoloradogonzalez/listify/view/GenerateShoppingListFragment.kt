package com.estebancoloradogonzalez.listify.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.estebancoloradogonzalez.listify.databinding.FragmentGenerateShoppingListBinding
import com.estebancoloradogonzalez.listify.utils.Messages
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.viewmodel.ShoppingListViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class GenerateShoppingListFragment : Fragment() {

    private var _binding: FragmentGenerateShoppingListBinding? = null
    private val binding get() = _binding!!
    private val args: GenerateShoppingListFragmentArgs by navArgs()
    private val shoppingListViewModel: ShoppingListViewModel by viewModels()
    private var userId = NumericConstants.LONG_NEGATIVE_ONE

    private var selectedDate: LocalDateTime? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenerateShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = args.userId

        binding.etDate.isFocusable = false
        binding.etDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnGenerate.setOnClickListener {
            val date = selectedDate
            if (date == null) {
                binding.tvDateError.text = Messages.ENTER_VALID_DATE_MESSAGE
                binding.tvDateError.visibility = View.VISIBLE
            } else {
                binding.tvDateError.visibility = View.GONE
                shoppingListViewModel.generateShoppingList(date, userId,
                    onError = { errorMessage ->
                        binding.tvDateError.text = errorMessage
                        binding.tvDateError.visibility = View.VISIBLE
                    },
                    onSuccess = {
                        binding.tvDateError.visibility = View.GONE
                        requireActivity().onBackPressed()
                    }
                )
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireContext(), { _, selYear, selMonth, selDay ->
            selectedDate = LocalDateTime.of(selYear, selMonth + 1, selDay, LocalTime.now().hour, LocalTime.now().minute)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            binding.etDate.setText(selectedDate?.format(formatter))
        }, year, month, day)
        datePicker.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
