package fragment

import android.app.Application
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import uk.ac.bbk.dcs.budgettracker.R
import uk.ac.bbk.dcs.budgettracker.databinding.FragmentEditExpensesBinding
import viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.ViewModelProvider

//factory class instantiating ExpenseViewModel with Application context
class ExpenseViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class EditExpensesFragment : Fragment() {
    //binding to access views in the fragment
    private var _binding: FragmentEditExpensesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(requireActivity().application)
    }
    //navigation arguments
    private val args: EditExpensesFragmentArgs by navArgs()
    //for selecting date
    private val calendar = Calendar.getInstance()


    // returns roo view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    //sets up  UI components and event listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePicker()
        setupCategorySpinner()
        loadExpenseData()
        setupSaveButton()
    }

    //initializes date picker
    private fun setupDatePicker() {
        binding.dateTextView.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    updateDateInView()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        updateDateInView()
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.dateTextView.text = sdf.format(calendar.time)
    }

    //Category spinner
    private fun setupCategorySpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        }
    }
    // Loads expense data into UI components for editing
    private fun loadExpenseData() {
        viewModel.getExpense(args.expenseId).observe(viewLifecycleOwner) { expense ->
            expense?.let {
                binding.titleEditText.setText(it.title)
                binding.amountEditText.setText(it.amount.toString())
                calendar.timeInMillis = it.date
                updateDateInView()
                val categoryPosition = (binding.categorySpinner.adapter as ArrayAdapter<String>).getPosition(it.category)
                binding.categorySpinner.setSelection(categoryPosition)
            }
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val amount = binding.amountEditText.text.toString().toDoubleOrNull()
            val category = binding.categorySpinner.selectedItem.toString()

            if (title.isNotBlank() && amount != null) {
                // using ViewModel to update expense
                viewModel.updateExpense(
                    id = args.expenseId,
                    title = title,
                    amount = amount,
                    date = calendar.timeInMillis,
                    category = category
                )
                //Category totals are refreshed
                viewModel.refreshCategoryTotals()
                //navigating back
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Cleaning up view binding instance
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}