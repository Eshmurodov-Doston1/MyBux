package com.example.mybux

import android.app.Application
import com.droidnet.DroidNet
import com.example.mybux.di.AppComponent
import com.example.mybux.di.DaggerAppComponent
import javax.inject.Inject

class App @Inject constructor():Application() {
    companion object{
        lateinit var appComponent:AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        DroidNet.init(this)
        appComponent = DaggerAppComponent.factory().create(this,applicationContext)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners()
    }
}