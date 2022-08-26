package com.tip_n_go.ui.custom

import android.content.Context
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.textfield.TextInputEditText
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.tip_n_go.R
import com.tip_n_go.databinding.CustomPhoneEditTextBinding
import com.tip_n_go.tools.GlideApp
import kotlin.properties.Delegates


class CustomPhoneEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    style: Int = 0
) : FrameLayout(context, attributeSet, style), CountryCodeChangeListener {

    private var _binding: CustomPhoneEditTextBinding? = null
    private val binding get() = _binding!!
    private var oldTextWatcher: InternationPhoneParser by Delegates.notNull()
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()
    private var oldCode = ""
    var editText: TextInputEditText by Delegates.notNull()

    init {
        _binding = CustomPhoneEditTextBinding.inflate(LayoutInflater.from(context), this, true)
        oldTextWatcher = InternationPhoneParser(this)
        editText = binding.editText
        editText.addTextChangedListener(oldTextWatcher)
        val enabledRes = R.styleable.CustomPhoneEditText_isEnabled
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CustomPhoneEditText, style, 0)
        val enabled = attributes.getBoolean(enabledRes, true)
        binding.editText.isEnabled = enabled
    }

    override fun onCountryCodeChanged(countryCode: String) {
        val resource = context.resources.getIdentifier(
            countryCode.lowercase(),
            "drawable",
            context.packageName
        )
        val flagIcon = AppCompatResources.getDrawable(context, resource)
        GlideApp.with(context)
            .load(flagIcon)
            .into(binding.iconFlag)
        val number = editText.editableText.toString().filter { it.isDigit() }
        editText.removeTextChangedListener(oldTextWatcher)
        editText.editableText.clear()
        val newTextWatcher = InternationPhoneParser(
            this,
            countryCode
        )
        oldTextWatcher = newTextWatcher
        editText.addTextChangedListener(newTextWatcher)
        editText.setText(number)
        editText.text?.length?.let { editText.setSelection(it) }
    }

    private inner class InternationPhoneParser(
        private val listener: CountryCodeChangeListener,
        countryCode: String = ""
    ) : PhoneNumberFormattingTextWatcher(countryCode) {

        private fun getCountryIsoCode(number: String): String? {
            val validatedNumber = if (number.startsWith("+")) number else "+$number"
            val phoneNumber = try {
                phoneNumberUtil.parse(validatedNumber, null)
            } catch (e: NumberParseException) {
                null
            } ?: return null

            return phoneNumberUtil.getRegionCodeForCountryCode(phoneNumber.countryCode)
        }

        override fun afterTextChanged(input: Editable?) {
            super.afterTextChanged(input)
            if (input.isNullOrEmpty()) {
                return
            }
            if (input[0].isDigit()) {
                input.replace(0, 0, "+")
            }
            val number = input.toString().filter { it.isDigit() }
            val countryCode = getCountryIsoCode(number)
            if (!countryCode.isNullOrEmpty()) {
                if (countryCode != oldCode) {
                    oldCode = countryCode
                    listener.onCountryCodeChanged(countryCode)
                }
            }
        }
    }
}

interface CountryCodeChangeListener {
    fun onCountryCodeChanged(countryCode: String)
}
