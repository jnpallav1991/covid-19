package com.carwale.covid.model

import java.text.DecimalFormat

data class Summary(
    val Countries: ArrayList<Country>?,
    val Date: String?,
    val Global: GlobalS?
) {
    data class Country(
        val Country: String?,
        val CountryCode: String?,
        val Date: String?,
        val NewConfirmed: Int?,
        val NewDeaths: Int?,
        val NewRecovered: Int?,
        val Slug: String?,
        var TotalConfirmed: Int?,
        var TotalDeaths: Int?,
        val TotalRecovered: Int?
    )
    {
        fun formatDecimal(number: Int?):String
        {
            val formatter = DecimalFormat("#,###,###")
            return formatter.format(number)
        }
    }

    data class GlobalS(
        val NewConfirmed: Int?,
        val NewDeaths: Int?,
        val NewRecovered: Int?,
        val TotalConfirmed: Int?,
        val TotalDeaths: Int?,
        val TotalRecovered: Int?
    )
    fun formatDecimal(number: Int?):String
    {
        val formatter = DecimalFormat("#,###,###")
        return formatter.format(number)
    }
}