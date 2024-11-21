package fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import entities.Expense
import uk.ac.bbk.dcs.budgettracker.R
import uk.ac.bbk.dcs.budgettracker.databinding.FragmentAddExpenseBinding
import viewmodel.DateViewModel
import viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseFragment : Fragment() {

    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!
    // DateViewModel manages data
    private val dateViewModel: DateViewModel by viewModels()
    // ExpenseViewModel manages expense data and its operations
    private val expenseViewModel: ExpenseViewModel by viewModels()

    //Inflates layout, sets ViewModels, lifecycleOwner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dateViewModel = dateViewModel
        binding.expenseViewModel = expenseViewModel

        // Initialize expense object for data binding
        binding.expense = Expense(id = 0, title = "", amount = 0.0, date = Date().time, category = "")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //saving expense
        binding.saveButton.setOnClickListener {
            saveExpense()
        }

        //for picking date
        binding.dateTextView.setOnClickListener {
            dateViewModel.showDatePicker(requireContext())
        }

        //go back button
        binding.backButton.setOnClickListener {
            navigateBack()
        }

        // TextWatcher used to make sure that title input field is not emptied
        // automatically after the date is picked
        binding.titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                dateViewModel.updateExpenseField("title", s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        // Update UI, if live changes noticed
        dateViewModel.expense.observe(viewLifecycleOwner) { updatedExpense ->
            updatedExpense?.let {
                binding.expense = it
                binding.dateTextView.text = formatDate(it.date)
            }
        }


    }

    private fun saveExpense() {
        val title = binding.titleEditText.text.toString()
        val amount = binding.amountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val category = binding.categorySpinner.selectedItem.toString()
        val date = binding.expense?.date ?: Date().time

        if (title.isBlank() || amount == 0.0 || category.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        expenseViewModel.insert(title, amount, date, category)
        Toast.makeText(context, "Expense saved", Toast.LENGTH_SHORT).show()
        navigateBack()
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.actionAddExpenseFragmentToExpenseListFragment)
    }

    private fun formatDate(date: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date(date))
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}