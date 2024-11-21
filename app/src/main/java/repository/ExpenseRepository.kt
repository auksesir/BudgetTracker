package repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import dao.CategoryTotalDao
import dao.ExpenseDao
import entities.CategoryTotal
import entities.Expense

//this class is responsible for handling data operations for expenses and category totals
//acts as a middle man between ViewModel and DAO classes
//@property expenseDao Data Access Object for Expense entities.
// @property categoryTotalDao Data Access Object for CategoryTotal entities
class ExpenseRepository(private val expenseDao: ExpenseDao, private val categoryTotalDao: CategoryTotalDao) {
    //observing changes in a list of all expenses
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()
    //observing changes in a list of category totals
    val categoryTotals: LiveData<List<CategoryTotal>> = categoryTotalDao.getCategoryTotals()


    /**
     * Inserting expense into database
     *
     * @param expense The expense to insert
     */
    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
        updateCategoryTotal(expense.category, expense.amount)
        Log.d(TAG, "Inserted new expense: $expense")
        refreshCategoryTotals()
    }

    /**
     * Updating an existing expense
     *
     * @param expense updated expense object
     */
    suspend fun update(expense: Expense) {
        val oldExpense = expenseDao.getExpenseById(expense.id)
        if (oldExpense != null) {
            expenseDao.update(expense)
            refreshCategoryTotals()
            if (oldExpense.category != expense.category) {
                updateCategoryTotal(oldExpense.category, -oldExpense.amount)
                updateCategoryTotal(expense.category, expense.amount)
            } else {
                val amountDifference = expense.amount - oldExpense.amount
                updateCategoryTotal(expense.category, amountDifference)
            }
            Log.d(TAG, "Updated expense: $expense")
            refreshCategoryTotals()
        } else {
            Log.e(TAG, "Failed to update expense: Expense with id ${expense.id} not found")
        }
    }

    /**
     * Deleting an expense from the database
     *
     * @param id id of the expense
     */
    suspend fun deleteExpense(id: Int) {
        val expense = expenseDao.getExpenseById(id)
        if (expense != null) {
            expenseDao.deleteExpense(id)
            updateCategoryTotal(expense.category, -expense.amount)
            Log.d(TAG, "Deleted expense with id: $id")
            refreshCategoryTotals()
        } else {
            Log.e(TAG, "Failed to delete expense: Expense with id $id not found")
        }
    }

    /**
     * Updating category total in the CategoryTotalDao
     *
     * @param category category name
     * @param amountChange change in amount to be applied to the category total
     */
    private suspend fun updateCategoryTotal(category: String, amountChange: Double) {
        val categoryTotal = categoryTotalDao.getCategoryTotalByName(category)
        if (categoryTotal != null) {
            categoryTotal.total += amountChange
            if (categoryTotal.total == 0.0) {
                categoryTotalDao.delete(categoryTotal)
            } else {
                categoryTotalDao.update(categoryTotal)
            }
        } else if (amountChange != 0.0) {
            categoryTotalDao.insert(CategoryTotal(category, amountChange))
        }
    }

    //Refreshing category totals by recalculating them and updating
    //CategoryTotalDao
    suspend fun refreshCategoryTotals() {
        val calculatedTotals = expenseDao.calculateCategoryTotals()
        categoryTotalDao.deleteAll()
        categoryTotalDao.insertAll(calculatedTotals)
    }

    //updating category totals depending on the number of expenses
    //clearing category totals from CategoryTotalDao if no expenses exist
    suspend fun checkAndUpdateCategoryTotals() {
        val expenseCount = expenseDao.getExpenseCount()
        Log.d(TAG, "Number of expenses: $expenseCount")

        if (expenseCount > 0) {
            refreshCategoryTotals()
        } else {
            categoryTotalDao.deleteAll()
        }
    }

    /**
     * Retrieving an expense by its id
     *
     * @param id id of the expense to retrieve
     * @return LiveData containing the expense
     */
    fun getExpense(id: Int): LiveData<Expense> = expenseDao.getExpense(id)

    companion object {
        private const val TAG = "ExpenseRepository"
    }
}
