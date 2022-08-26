package com.tip_n_go.ui.adapter

import android.content.Context
import android.widget.ArrayAdapter

class HintAdapter<T>(context: Context, resource: Int, objects: Array<T>) :
    ArrayAdapter<T>(context, resource, objects) {

    override fun getCount(): Int {
        val count = super.getCount()
        return if (count > 0) count - 1 else count
    }
}
