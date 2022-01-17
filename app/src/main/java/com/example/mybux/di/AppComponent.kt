package com.example.mybux.di

import android.content.Context
import com.example.mybux.App
import com.example.mybux.presentation.MainActivity
import com.example.mybux.di.modules.DataModule
import com.example.mybux.di.modules.DatabaseModule
import com.example.mybux.di.modules.NetworkModule
import com.example.mybux.di.modules.ViewModuleModule
import com.example.mybux.ui.auth.AuthFragment
import com.example.mybux.ui.auth.AuthLanguageFragment
import com.example.mybux.ui.basket.BasketFragment
import com.example.mybux.ui.discount.DiscountFragment
import com.example.mybux.ui.gallery.GalleryFragment
import com.example.mybux.ui.gallery.cashFragment.CashListFragment
import com.example.mybux.ui.home.HomeFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class,NetworkModule::class,ViewModuleModule::class,DatabaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{ fun create(@BindsInstance app:App,@BindsInstance context: Context):AppComponent }

    fun inject(mainActivity: MainActivity)
    fun inject(authFragment: AuthFragment)
    fun inject(authLanguageFragment: AuthLanguageFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(basketFragment: BasketFragment)
    fun inject(discountFragment: DiscountFragment)
    fun inject(galleryFragment: GalleryFragment)
    fun inject(cashListFragment: CashListFragment)
}