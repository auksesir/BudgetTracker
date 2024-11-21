package viewmodel

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import entities.Expense
import java.util.Calendar
import java.util.Date

/**
 * ViewModel for managing date-related operations and expense data
 *
 * @param application application context needed for AndroidViewModel
 */
class DateViewModel(application: Application) : AndroidViewModel(application) {
    val expense: MutableLiveData<Expense?> = MutableLiveData(Expense(id = 0, title = "", amount = 0.0, date = Date().time, category = ""))

    /**
     * Showing DatePickerDialog to select a date and updates the expense's date
     *
     * @param context context to show the DatePickerDialog
     */
    fun showDatePicker(context: Context) {
        val calendar = Calendar.getInstance()
        expense.value?.let { currentExpense ->
            calendar.timeInMillis = currentExpense.date
        }

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)
                }
                val updatedExpense = expense.value?.copy(date = selectedCalendar.timeInMillis)
                expense.value = updatedExpense
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    /**
     * Updating specific field of the expense that is being edited or added
     *
     * @param field field name to update "title", "amount", "category"
     * @param value new value for the specified field
     */
    fun updateExpenseField(field: String, value: Any) {
        val currentExpense = expense.value ?: Expense(id = 0, title = "", amount = 0.0, date = Date().time, category = "")
        val updatedExpense = when (field) {
            "title" -> currentExpense.copy(title = value as String)
            "amount" -> currentExpense.copy(amount = value as Double)
            "category" -> currentExpense.copy(category = value as String)
            else -> currentExpense
        }
        expense.value = updatedExpense
    }
}


