package com.example.mybux.presentation

import android.content.res.Configuration
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import com.example.mybux.theme.MyAppTheme

interface SetListener {
    fun viewVisible()
    fun viewGone()
    fun setText(str:String)
    fun setText1(str:String)
    fun setText1Visible(str:String)
    fun setToolbar()
    fun setToolbar1()
    fun setToolbarGone()
    fun oneVisible()
    fun toolBarVisible()
    fun showLoading()
    fun hideLoading()
    fun getToolbar():Toolbar
    fun toolbarGone()
    fun getConfiguration(configuration: Configuration?)
    fun toolbar()
    fun myAppTheme():LiveData<MyAppTheme>
    fun hideToolbar()
    fun getDrawer():DrawerLayout
    fun toolbarTitleColor(): TextView
    fun internetConnection():LiveData<Boolean>
    fun closeDrawer()
}