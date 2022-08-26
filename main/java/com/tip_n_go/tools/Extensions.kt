package com.tip_n_go.tools

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tip_n_go.R
import com.tip_n_go.state.UiState
import com.tip_n_go.ui.adapter.HintAdapter
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.getDrawable(resourceId: Int): Drawable? {
    return ContextCompat.getDrawable(this.requireContext(), resourceId)
}

fun Context.getDrawableResource(resourceId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resourceId)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun EditText.showKeyboard() {
    context.showKeyboard(this)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}

inline fun <reified T> Any.listCastCheck(consumer: (List<T>) -> Unit) {
    if (this is List<*>) {
        if (this.isNotEmpty()) {
            if (this.first() is T) {
                @Suppress("UNCHECKED_CAST")
                consumer(this as List<T>)
            }
        } else {
            consumer(this as List<T>)
        }
    }
}

// todo locale
fun String.prepareDate(locale: Locale = Locale.getDefault()): String {
    val oldFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", locale)
    val oldDate = oldFormat.parse(this)
    val newFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", locale)
    return newFormat.format(oldDate!!)
}

// todo
val TextInputLayout.getText get() = this.editText?.text.toString()

// todo locale
fun Int.prepareCurrency(): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    format.maximumFractionDigits = 2
    return format.format(this.toDouble() / 100)
}

fun Double.prepareCurrency(): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    format.maximumFractionDigits = 2
    return format.format(this)
}

fun Int.prepareTipRate(): String {
    val value = this.toDouble() / 100
    return "$value%"
}

// todo decimal point locale
fun Int.prepareRate(): Double {
    return this.toDouble() / 100
}

fun Int.prepareAmount(): Double {
    return this.toDouble() / 100
}

fun TextView.setRatingWithDrawableEnd(rating: Double = 0.0) {
    val drawable = if (rating >= 0.0 && rating < 1.5) {
        context?.getDrawableResource(R.drawable.empty_star)
    } else if (rating >= 1.5 && rating < 2.5) {
        context?.getDrawableResource(R.drawable.one_star)
    } else if (rating >= 2.5 && rating < 3.5) {
        context?.getDrawableResource(R.drawable.two_star)
    } else if (rating >= 3.5 && rating < 4.5) {
        context?.getDrawableResource(R.drawable.three_star)
    } else {
        context?.getDrawableResource(R.drawable.full_star)
    }

    if (rating >= 0.0) {
        this.text = rating.toString()
    }

    this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    if (rating < 0.5) {
        if (this.compoundDrawables.size > 2) {
            this.compoundDrawables[2].setTint(
                ContextCompat.getColor(
                    context,
                    R.color.grey
                )
            )
        }
    }
}

fun Fragment.createHintAdapter(mList: MutableList<String?>): HintAdapter<String?> {
    mList.add("")
    val array = mList.toTypedArray()
    val adapter = HintAdapter(requireContext(), R.layout.item_custom_spinner, array)
    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
    return adapter
}

fun View.showSnack(message: String) {
    val snack = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snack.anchorView = this
    snack.show()
}

val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()


fun UiState.Error.showError(view: View){
    val validationErrors = this.exception.validationErrors
    if (validationErrors != null){
        view.showSnack(validationErrors.additionalProp1)
    } else {
        val message = this.exception.error
        view.showSnack(message)
    }
}