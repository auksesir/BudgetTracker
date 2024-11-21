package viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import database.ExpenseDatabase
import entities.CategoryTotal
import entities.Expense
import kotlinx.coroutines.launch
import repository.ExpenseRepository

/**
 * ViewModel for managing expense data and interacting with repository
 *
 * @param application application context for AndroidViewModel
 */
class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpenseRepository
    val allExpenses: LiveData<List<Expense>>
    val categoryTotals: LiveData<List<CategoryTotal>>

    init {
        val expenseDatabase = ExpenseDatabase.getDatabase(application)
        val expenseDao = expenseDatabase.expenseDao()
        val categoryTotalDao = expenseDatabase.categoryTotalDao()
        repository = ExpenseRepository(expenseDao, categoryTotalDao)
        allExpenses = repository.allExpenses
        categoryTotals = repository.categoryTotals
        categoryTotals.observeForever { totals ->
            Log.d(TAG, "ViewModel received category totals: ${totals.map { "CategoryTotal(category=${it.category}, total=${it.total})" }}")
        }
    }

    //initiating coroutine and updating category totals
    fun checkAndUpdateCategoryTotals() = viewModelScope.launch {
        repository.checkAndUpdateCategoryTotals()
    }

    //initiating coroutine and refreshing category totals
    fun refreshCategoryTotals() = viewModelScope.launch {
        repository.refreshCategoryTotals()
    }

    /**
     * Initiating coroutine to insert a new expense
     *
     * @param title expense title
     * @param amount expense amount
     * @param date expense date
     * @param category expense category
     */
    fun insert(title: String, amount: Double, date: Long, category: String) = viewModelScope.launch {
        val newExpense = Expense(title = title, amount = amount, date = date, category = category)
        repository.insert(newExpense)
    }

    /**
     * Initiating coroutine to update an existing expense
     *
     * @param id expense id
     * @param title new expense title
     * @param amount new expense amount
     * @param date new expense date
     * @param category new expense category
     */
    fun updateExpense(id: Int, title: String, amount: Double, date: Long, category: String) = viewModelScope.launch {
        val updatedExpense = Expense(id = id, title = title, amount = amount, date = date, category = category)
        repository.update(updatedExpense)
    }

    /**
     * Retrieving expense by its id
     *
     * @param id expense id
     * @return LiveData containing the expense
     */
    fun getExpense(id: Int): LiveData<Expense> {
        return repository.getExpense(id)
    }

    /**
     * Initiating coroutine to delete an expense
     *
     * @param id expense id
     */
    fun deleteExpense(id: Int) = viewModelScope.launch {
        repository.deleteExpense(id)
    }

    companion object {
        private const val TAG = "ExpenseViewModel"
    }
}
