package com.example.mybux.theme

import android.content.Context
import com.dolatkia.animatedThemeManager.AppTheme

interface MyAppTheme:AppTheme {
    fun ActivityBackgroundColor(context: Context):Int
    fun textColor(context: Context):Int
    fun iconsColor(context: Context):Int
    fun buttonBackground(context: Context):Int
    fun cardBackground(context: Context):Int
    fun toolbarColor(context: Context):Int
    fun backSearchView(context: Context):Int
    fun textHintColor(context: Context):Int
    fun logingTextColor(context: Context):Int
    fun hintColor(context: Context):Int
    fun buttonBack(context: Context):Int
    fun buttonLangColor(context: Context):Int
    fun statusBarColor(context: Context):Int
    fun priceBtnColor(context: Context):Int
    fun clearBasketText(context: Context):Int
    fun clearBasketIconColor(context: Context):Int
    fun spinnerColor(context: Context):Int
}