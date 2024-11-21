package fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.ac.bbk.dcs.budgettracker.databinding.FragmentCategoryTotalsBinding
import viewmodel.ExpenseViewModel
import adapter.CategoryTotalsAdapter

class CategoryTotalsFragment : Fragment() {

    private var _binding: FragmentCategoryTotalsBinding? = null
    private val binding get() = _binding!!
    // for managing expense data and their operations
    private val expenseViewModel: ExpenseViewModel by viewModels()
    // used to display category totals
    private lateinit var adapter: CategoryTotalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryTotalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // triggers database query initially
        expenseViewModel.checkAndUpdateCategoryTotals()

        // notices live changes for category totals
        expenseViewModel.categoryTotals.observe(viewLifecycleOwner) { totals ->
            Log.d("CategoryTotalsFragment", "Received category totals: $totals")
            totals?.let {
                //updating adapter with the new list
                adapter.submitList(it)
                if (it.isEmpty()) {
                    binding.emptyStateTextView.visibility = View.VISIBLE
                } else {
                    binding.emptyStateTextView.visibility = View.GONE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = CategoryTotalsAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CategoryTotalsFragment.adapter
        }
    }

    //refresh on resume of the fragment
    override fun onResume() {
        super.onResume()
        expenseViewModel.refreshCategoryTotals()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
