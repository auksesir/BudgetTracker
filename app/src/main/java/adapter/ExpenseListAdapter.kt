package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.ac.bbk.dcs.budgettracker.databinding.ItemExpenseBinding
import entities.Expense


// Used to display Expenses in a RecyclerView
class ExpenseListAdapter : ListAdapter<Expense, ExpenseListAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    // holds click listener
    private var onItemClickListener: ((Expense) -> Unit)? = null

    //sets click listener
    fun setOnItemClickListener(listener: (Expense) -> Unit) {
        onItemClickListener = listener
    }
    //creates ViewHolder if no exists
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        //each item inflates space
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    //Binding data to the VieHolder
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        holder.bind(expense)
    }

    //ViewHolder class that holds references to the views for each Expense
    inner class ExpenseViewHolder(private val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            binding.expense = expense
            binding.executePendingBindings()
            //sets click listener
            itemView.setOnClickListener {
                onItemClickListener?.invoke(expense) // invokes click listener
            }
        }
    }
}
