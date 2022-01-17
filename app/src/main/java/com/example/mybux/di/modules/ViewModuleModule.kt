package com.example.mybux.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybux.viewModul.ViewModelAuth
import com.example.mybux.viewModul.ViewModelFactory
import com.example.mybux.viewModul.ViewModelKey
import com.example.mybux.viewModul.ViewModelProduct
import com.example.mybux.viewModul.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModuleModule {
    @Binds
    @IntoMap
    @ViewModelKey(ViewModelAuth::class)
    abstract fun bindsAuthViewModel(viewModel:ViewModelAuth):ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ViewModelProduct::class)
    abstract fun bindsGetAllProductViewModel(viewModel:ViewModelProduct):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindsSettingsAuthViewModel(viewModel:SettingsViewModel):ViewModel


    @Binds
    abstract fun bindsViewModelFactory(viewModelsFactory:ViewModelFactory):ViewModelProvider.Factory
}