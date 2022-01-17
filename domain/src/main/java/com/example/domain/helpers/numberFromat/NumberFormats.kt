package com.example.domain.helpers.numberFromat

import java.text.DecimalFormat

object NumberFormats {
    fun parseNumberFormat(number: Double): String? {
        val formatter = DecimalFormat("###,###,###")
        return formatter.format(number)
//        NumberFormat nf2 = NumberFormat.getCurrencyInstance();
//        return nf2.format(number);
    }
}