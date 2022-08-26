package com.tip_n_go.data.incoming

import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.SeriesDeserializers
import kotlinx.parcelize.Parcelize

@Parcelize
class ReportData(
    val title: Title? = null,
    val type: String? = null,
    val yAxis: YAxis? = null,
    val xAxis: XAxis? = null,
    val series: List<Series> = listOf()
) : Parcelable

@Parcelize
class Title(
    val text: String? = null
) : Parcelable

@Parcelize
class YAxis(
    val title: Title? = null
) : Parcelable

@Parcelize
class XAxis(
    val categories: List<String>? = null
) : Parcelable

@Parcelize
@JsonAdapter(SeriesDeserializers::class)
class Series(
    val name: String? = null,
    val data: List<Double>? = null,
    val dataPie: List<DataPie>? = null,
    val type: String? = null,
    val color: String? = null
) : Parcelable

@Parcelize
class DataPie(
    val name: String? = null,
    val y: Int? = null,
    val color: String? = null
) : Parcelable {
    override fun toString(): String {
        return "name: $name, y: $y, color: $color"
    }
}
