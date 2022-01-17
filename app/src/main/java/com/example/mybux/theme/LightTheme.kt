package com.example.mybux.theme

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.mybux.R

class LightTheme:MyAppTheme {
    override fun ActivityBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.background)
    }

    override fun textColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.dayTextColor)
    }

    override fun iconsColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.iconColorDay)
    }

    override fun buttonBackground(context: Context): Int {
        return ContextCompat.getColor(context, R.color.myButtonDayColor)
    }

    override fun cardBackground(context: Context): Int {
        return ContextCompat.getColor(context, R.color.cardBack)
    }

    override fun toolbarColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.toolbarColorDay)
    }

    override fun backSearchView(context: Context): Int {
        return ContextCompat.getColor(context,R.color.searchBackDay)
    }

    override fun textHintColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.hintTextColorDay)
    }

    override fun logingTextColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.loginTextColorDay)
    }

    override fun hintColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.hintTextColorDay)
    }

    override fun buttonBack(context: Context): Int {
        return ContextCompat.getColor(context,R.color.buttonBackDay)
    }

    override fun buttonLangColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.lanCardButtonDay)
    }

    override fun statusBarColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.statusBarColorDay)
    }

    override fun priceBtnColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.priceDayColor)
    }

    override fun clearBasketText(context: Context): Int {
        return ContextCompat.getColor(context,R.color.clearBasketTextDay)
    }

    override fun clearBasketIconColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.clearBasketIconDay)
    }

    override fun spinnerColor(context: Context): Int {
        return ContextCompat.getColor(context,R.color.spinnerColorDay)
    }

    override fun id(): Int {
        return 0
    }
}