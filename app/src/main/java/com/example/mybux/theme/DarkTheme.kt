package com.example.mybux.theme

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.mybux.R

class DarkTheme:MyAppTheme {
    override fun ActivityBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.backgroundDark)
    }

    override fun textColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.darkTextColor)
    }

    override fun iconsColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.iconColorDark)
    }

    override fun buttonBackground(context: Context): Int {
        return ContextCompat.getColor(context, R.color.myButtondarkColor)
    }

    override fun cardBackground(context: Context): Int {
        return ContextCompat.getColor(context, R.color.cardBackDark)
    }

    override fun toolbarColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.toolbarColordark)
    }

    override fun backSearchView(context: Context): Int {
        return ContextCompat.getColor(context,R.color.searchBackNight)
    }

    override fun textHintColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.hintTextColorNight)
    }

    override fun logingTextColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.loginTextColorNight)
    }

    override fun hintColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.hintTextColorNight)
    }

    override fun buttonBack(context: Context): Int {
        return ContextCompat.getColor(context,R.color.buttonBackNight)
    }

    override fun buttonLangColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.lanCardButtonNight)
    }

    override fun statusBarColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.statusBarColorNight)
    }

    override fun priceBtnColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.priceNightColor)
    }

    override fun clearBasketText(context: Context): Int {
        return ContextCompat.getColor(context,R.color.clearBasketTextNight)
    }

    override fun clearBasketIconColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.clearBasketIconNight)
    }

    override fun spinnerColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.spinnerColorNight)
    }

    override fun id(): Int {
        return 1
    }
}