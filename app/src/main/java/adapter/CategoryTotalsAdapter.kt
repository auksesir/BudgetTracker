package adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.ac.bbk.dcs.budgettracker.databinding.ItemCategoryTotalBinding
import entities.CategoryTotal

//Class to display CategoryTotal items in a RecyclerView
class CategoryTotalsAdapter : ListAdapter<CategoryTotal, CategoryTotalsAdapter.ViewHolder>(
    //defined at the bottom of the class
    CategoryTotalDiffCallback() // passed to the ListAdapter to compare old and new lists
) {

    //creates ViewHolder if it doesn't exist
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //more space created for each item
        val binding = ItemCategoryTotalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

   //binds categoryTotal data to the View holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryTotal = getItem(position)
        holder.bind(categoryTotal)
    }

    //Holds reference to the views for each category to be displayed
    class ViewHolder(private val binding: ItemCategoryTotalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryTotal: CategoryTotal) {
            binding.categoryNameTextView.text = categoryTotal.category
            binding.categoryTotalTextView.text = String.format("%.2f", categoryTotal.total)
        }
    }

    private class CategoryTotalDiffCallback : DiffUtil.ItemCallback<CategoryTotal>() {
        override fun areItemsTheSame(oldItem: CategoryTotal, newItem: CategoryTotal): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: CategoryTotal, newItem: CategoryTotal): Boolean {
            return oldItem == newItem
        }
    }
}