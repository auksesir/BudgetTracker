package fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uk.ac.bbk.dcs.budgettracker.R
import uk.ac.bbk.dcs.budgettracker.databinding.FragmentExpenseListBinding
import adapter.ExpenseListAdapter
import viewmodel.ExpenseViewModel
import androidx.fragment.app.activityViewModels

class ExpenseListFragment : Fragment() {
    //ViewModel for managing expense related data
    private val viewModel: ExpenseViewModel by activityViewModels()
    private var _binding: FragmentExpenseListBinding? = null
    // for accessing views in the fragment
    private val binding get() = _binding!!
    //for RecyclerView to display expenses
    private lateinit var adapter: ExpenseListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        //observing live data
        observeExpenses()
        //listener for a add new expense button
        setupFabClickListener()
    }

    //setting up RecyclerView with LinearLayoutManager and ExpenseListAdapter
    //for setting click listener to navigate to ExpenseDetailsFragment
    private fun setupRecyclerView() {
        adapter = ExpenseListAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { expense ->
            // Navigate to detail fragment of selected expense
            val action = ExpenseListFragmentDirections.actionExpenseListFragmentToExpenseDetailsFragment(expense.id)
            findNavController().navigate(action)
        }
    }

    //updating adapter when live data changes
    private fun observeExpenses() {
        viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
        }
    }

    //click listener for a add new expense button
    private fun setupFabClickListener() {
        binding.fab.setOnClickListener {
            // Navigate to add expense fragment
            findNavController().navigate(R.id.actionExpenseListFragmentToAddExpenseFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
