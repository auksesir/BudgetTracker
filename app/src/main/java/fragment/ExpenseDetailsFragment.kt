package fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import uk.ac.bbk.dcs.budgettracker.databinding.FragmentExpenseDetailsBinding
import viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.fragment.app.activityViewModels

class ExpenseDetailsFragment : Fragment() {
    // navigation args from previous fragment
    private val args: ExpenseDetailsFragmentArgs by navArgs()
    //for managing expense related data
    private val viewModel: ExpenseViewModel by activityViewModels()
    // for accessing views in the fragment
    private var _binding: FragmentExpenseDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExpenseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //expenseId from navigation args
        val expenseId = args.expenseId
        //for updating UI and observing Live Data
        viewModel.getExpense(expenseId).observe(viewLifecycleOwner) { expense ->
            expense?.let {
                binding.titleTextView.text = it.title
                binding.amountTextView.text = String.format("$%.2f", it.amount)
                binding.dateTextView.text = formatDate(it.date)
                binding.categoryTextView.text = it.category
            }
        }

        //Edit button
        binding.editButton.setOnClickListener {
            val action = ExpenseDetailsFragmentDirections.actionExpenseDetailsFragmentToEditExpensesFragment(expenseId)
            findNavController().navigate(action)
        }



        binding.deleteButton.setOnClickListener {
            viewModel.deleteExpense(expenseId)
            findNavController().navigateUp()
        }


    }

    private fun formatDate(date: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date(date))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}