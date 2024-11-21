package utils

import android.annotation.SuppressLint
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//an object wit custom binding adapters used in AndroidViews
object BindingAdapters {
    /**
     * Setting formatted date text to a TextView using data binding
     *
     * @param view TextView to bind the formatted date text
     * @param date date value formatted and displayed
     */
    @JvmStatic
    @BindingAdapter("app:dateFormatted")
    fun setFormattedDate(view: TextView, date: Long) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        view.text = dateFormat.format(Date(date))
    }

    /**
     * Setting double value to an EditText using data binding, formatted to two decimal places
     *
     * @param view EditText to bind the value
     * @param value value to be displayed
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    @BindingAdapter("app:doubleValue")
    fun setDoubleValue(view: EditText, value: Double) {
        val stringValue = if (value == 0.0) "" else String.format("%.2f", value)
        if (view.text.toString() != stringValue) {
            view.setText(stringValue)
        }
    }

    /**
     * Setting expense title to an EditText using data binding
     *
     * @param view EditText to bind the expense title
     * @param title title to be displayed
     */
    @JvmStatic
    @BindingAdapter("app:expenseTitle")
    fun setExpenseTitle(view: EditText, title: String?) {
        if (view.text.toString() != title) {
            view.setText(title)
        }
    }
}



