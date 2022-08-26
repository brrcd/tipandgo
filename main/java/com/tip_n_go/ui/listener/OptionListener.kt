package com.tip_n_go.ui.listener

import android.widget.CheckBox

interface OptionListener {
    fun onOptionSelected(position: Int, optionId: Int, isChecked: Boolean)
    fun onOptionViewCreated(view: CheckBox)
}
