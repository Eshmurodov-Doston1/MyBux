package com.example.mybux.presentation

import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.view.*
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.utils.ViewState
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.Coordinate
import com.dolatkia.animatedThemeManager.ThemeActivity
import com.dolatkia.animatedThemeManager.ThemeManager
import com.droidnet.BuildConfig
import com.droidnet.DroidListener
import com.example.mybux.R
import com.example.mybux.databinding.ActivityMainBinding
import com.telpo.tps550.api.reader.SmartCardReader
import com.droidnet.DroidNet
import com.example.domain.language.LocaleManager
import com.example.mybux.App
import com.example.mybux.databinding.NavHeaderMainBinding
import com.example.mybux.theme.DarkTheme
import com.example.mybux.theme.LightTheme
import com.example.mybux.theme.MyAppTheme
import com.example.mybux.theme.MyThemeAnimationListener
import com.example.mybux.ui.gallery.listener.LanguageListener
import com.example.mybux.viewModul.ViewModelAuth
import com.example.mybux.viewModul.ViewModelProduct
import com.matrixxun.starry.badgetextview.MaterialBadgeTextView
import es.dmoral.toasty.Toasty
import java.util.concurrent.Executor
import javax.inject.Inject
import com.github.ivbaranov.mfb.MaterialFavoriteButton





class MainActivity : ThemeActivity(),DroidListener,SetListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var mDroidNet: DroidNet
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencesMode: SharedPreferences
    @Inject
    lateinit var factory:ViewModelProvider.Factory

    lateinit var myAppThemeLive:MutableLiveData<MyAppTheme>
    lateinit var myInternetConnection:MutableLiveData<Boolean>

    private val viewModel:ViewModelAuth by viewModels{factory}

    private val viewModelProduct:ViewModelProduct by viewModels{factory}
    lateinit var myAppTheme:MyAppTheme
    private val reader by lazy { SmartCardReader(this, SmartCardReader.SLOT_PSAM1) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        myInternetConnection = MutableLiveData()
        val favorite = MaterialFavoriteButton.Builder(this).create()

        myAppThemeLive = MutableLiveData()




        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("settingsParol",0)

        sharedPreferencesMode = getSharedPreferences("mode",0)
        mDroidNet = DroidNet.getInstance()
        mDroidNet.addInternetConnectivityListener(this);
        LocaleManager.setLocale(this )
        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_gallery), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val navHeaderMainBinding = NavHeaderMainBinding.bind(navView.getHeaderView(0))


        viewModel.getAuthDatabase().getAllAuth().observe(this, {
            if(it!=null){
                if (it.shortname.isNotEmpty() && it.inn.isNotEmpty()){
                    binding.appBarMain.contentMain.includeLoading.cons.visibility= View.GONE
                    navHeaderMainBinding.nameCompany.text = it.shortname
                    navHeaderMainBinding.innGroup.text = it.inn
                }else{
                    binding.appBarMain.contentMain.includeLoading.cons.visibility= View.VISIBLE
                }
            }
        })
        setThemeAnimationListener(MyThemeAnimationListener(this@MainActivity))
        val boolean = sharedPreferencesMode.getBoolean("modeApp", false)
        if (boolean){
            navHeaderMainBinding.mode.isFavorite = true
            ThemeManager.instance.getCurrentLiveTheme().observe(this@MainActivity) {theme->
                navView.itemTextColor = getColorStateList(R.color.icons_color_dark)
                navView.itemIconTintList = getColorStateList(R.color.icons_color_dark)
                val actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.clouse)
                binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
                actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
                actionBarDrawerToggle.syncState()
                binding.appBarMain.toolbar.setTitleTextColor(Color.WHITE)
                binding.appBarMain.basketText.setTextColor(Color.WHITE)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this,R.color.toolbarColordark)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE
            }
        }else{
            navHeaderMainBinding.mode.isFavorite = false
            ThemeManager.instance.getCurrentLiveTheme().observe(this@MainActivity) {
                navView.itemTextColor = getColorStateList(R.color.icons_color)
                navView.itemIconTintList = getColorStateList(R.color.icons_color)
                binding.appBarMain.toolbar.navigationIcon?.setColorFilter(Color.BLACK,PorterDuff.Mode.MULTIPLY)
                binding.appBarMain.toolbar.setTitleTextColor(Color.BLACK)
                binding.appBarMain.basketText.setTextColor(Color.BLACK)
                window.statusBarColor = ContextCompat.getColor(this,R.color.toolbarColorDay)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        navHeaderMainBinding.mode.setOnFavoriteAnimationEndListener { buttonView, favorite ->
            if (favorite){
                var modeSharedPref = sharedPreferencesMode.edit()
                modeSharedPref.putBoolean("modeApp",true)
                modeSharedPref.apply()

                ThemeManager.instance.changeTheme(DarkTheme(), Coordinate(10, 20))
                ThemeManager.instance.getCurrentLiveTheme().observe(this@MainActivity) {
                    navView.itemTextColor = getColorStateList(R.color.icons_color_dark)
                    navView.itemIconTintList = getColorStateList(R.color.icons_color_dark)

                    val actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.clouse)

                    binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
                    actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
                    actionBarDrawerToggle.syncState()

                    binding.appBarMain.toolbar.setTitleTextColor(Color.WHITE)
                    binding.appBarMain.basketText.setTextColor(Color.WHITE)


                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = ContextCompat.getColor(this,R.color.toolbarColordark)
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE
                }
            }else {
                var modeSharedPref = sharedPreferencesMode.edit()
                modeSharedPref.putBoolean("modeApp",false)
                modeSharedPref.apply()
                ThemeManager.instance.changeTheme(LightTheme(), buttonView, 800)
                ThemeManager.instance.getCurrentLiveTheme().observe(this@MainActivity) {
                    navView.itemTextColor = getColorStateList(R.color.icons_color)
                    navView.itemIconTintList = getColorStateList(R.color.icons_color)
                    binding.appBarMain.toolbar.navigationIcon?.setColorFilter(Color.BLACK,PorterDuff.Mode.MULTIPLY)
                    binding.appBarMain.toolbar.setTitleTextColor(Color.BLACK)
                    binding.appBarMain.basketText.setTextColor(Color.BLACK)
                    window.statusBarColor = ContextCompat.getColor(this,R.color.toolbarColorDay)
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }

        val database = viewModelProduct.getDatabase()

        binding.appBarMain.consToolbar.setOnClickListener {
            val basketListMy = database.getBasketListMy()
            if(basketListMy.isNotEmpty()){
                navController.navigate(R.id.basketFragment)
                binding.appBarMain.basketText.visibility = View.GONE
                binding.appBarMain.activeCount.visibility = View.GONE
            }else{
                Toasty.error(this, getString(R.string.no_basket), Toasty.LENGTH_LONG, true).show()
            }

        }
        binding.appBarMain.basketText.text = getString(R.string.basket)
        database.getBasketList().observe(this,{
            if (it.isEmpty()){
                binding.appBarMain.activeCount.visibility = View.GONE
            }else{
                if (navController.currentDestination?.id==R.id.nav_home){
                    binding.appBarMain.activeCount.text = it.size.toString()
                    binding.appBarMain.activeCount.visibility = View.VISIBLE
                }
            }
        })

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun syncTheme(appTheme: AppTheme) {
        myAppTheme = appTheme as MyAppTheme
        myAppThemeLive = MutableLiveData()
        myAppThemeLive.postValue(myAppTheme)
        val navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        binding.apply {
            myAppTheme.ActivityBackgroundColor(this@MainActivity).let { drawerLayout.setBackgroundColor(it) }
            myAppTheme.ActivityBackgroundColor(this@MainActivity).let { navView.setBackgroundColor(it) }
            myAppTheme.ActivityBackgroundColor(this@MainActivity).let { navHeaderMainBinding.backnav.setBackgroundColor(it) }
            myAppTheme.textColor(this@MainActivity).let { navHeaderMainBinding.nameCompany.setTextColor(it) }
            myAppTheme.textColor(this@MainActivity).let { navHeaderMainBinding.innGroup.setTextColor(it) }
            navView.setItemBackgroundResource(R.drawable.nav_view_item_back)
            myAppTheme.toolbarColor(this@MainActivity).let { appBarMain.toolbar.setBackgroundColor(it) }
        }
    }

    override fun myAppTheme(): LiveData<MyAppTheme> {
        return myAppThemeLive
    }
    override fun getStartTheme(): AppTheme {
        sharedPreferencesMode = getSharedPreferences("mode",0)
        val boolean = sharedPreferencesMode.getBoolean("modeApp", false)
        if (boolean){
            return DarkTheme()
        }else{
            return LightTheme()
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen){
            binding.drawerLayout.close()
        }else{
            val navController = findNavController(R.id.fragment)
            if (navController.currentDestination?.id==R.id.nav_home){
                finish()
            }else if (navController.currentDestination?.id==R.id.authFragment){
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.authFragment, false).build()
                navController.navigate(R.id.authLanguageFragment,null,navOptions)
            }else if (navController.currentDestination?.id!=R.id.authLanguageFragment){
                navController.popBackStack()
            }else if (navController.currentDestination?.id!=R.id.nav_gallery){
                val edit = sharedPreferences.edit()
                edit.clear()
                edit.apply()
                navController.popBackStack()
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return findNavController(R.id.fragment).navigateUp()
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        myInternetConnection.postValue(isConnected)
    }

    override fun onDestroy() {
        super.onDestroy()
        val edit = sharedPreferences.edit()
        edit.clear()
        edit.apply()
        mDroidNet.removeInternetConnectivityChangeListener(this);
    }

    override fun onStop() {
        super.onStop()
        val edit = sharedPreferences.edit()
        edit.clear()
        edit.apply()
    }


    override fun viewVisible() {
        binding.apply {
            appBarMain.basketText.visibility = View.VISIBLE
            appBarMain.activeCount.visibility = View.VISIBLE
            appBarMain.text.visibility = View.GONE
        }
    }

    override fun viewGone() {
        binding.apply {
            appBarMain.basketText.visibility = View.GONE
            appBarMain.activeCount.visibility = View.GONE
        }
    }

    override fun setText(str: String) {
        binding.apply {
            appBarMain.text.visibility = View.VISIBLE
            appBarMain.text.text = str
        }
    }

    override fun toolbarTitleColor():TextView {
        return binding.appBarMain.text
    }

    override fun setText1(str: String) {
        binding.apply {
            appBarMain.consToolbar.visibility = View.GONE
            appBarMain.toolbar.title = str
        }
    }

    override fun getDrawer(): DrawerLayout {
     return binding.drawerLayout
    }
    override fun setToolbar() {
        binding.apply {
            appBarMain.basketText.visibility = View.VISIBLE
            appBarMain.activeCount.visibility = View.VISIBLE
            appBarMain.toolbar.isEnabled =false
        }
    }

    override fun setToolbarGone() {
        binding.apply {
            appBarMain.basketText.visibility = View.GONE
            appBarMain.activeCount.visibility = View.GONE
            appBarMain.toolbar.isEnabled =false
            appBarMain.consToolbar.isEnabled =false
        }
    }

    override fun hideToolbar() {
        binding.apply {
            appBarMain.basketText.visibility = View.GONE
            appBarMain.activeCount.visibility = View.GONE
            appBarMain.toolbar.isEnabled =false
            appBarMain.consToolbar.isEnabled =false
            appBarMain.appbar.visibility =View.GONE
        }
    }

    override fun setToolbar1() {
        binding.apply {
            appBarMain.basketText.visibility = View.VISIBLE
            appBarMain.activeCount.visibility = View.VISIBLE
            appBarMain.toolbar.isEnabled =true
        }
    }

    override fun setText1Visible(str: String) {
        binding.apply {
            appBarMain.consToolbar.visibility = View.VISIBLE
            appBarMain.toolbar.title = ""
        }
    }

    override fun oneVisible() {
       binding.apply {
           appBarMain.basketText.visibility = View.VISIBLE
           appBarMain.activeCount.visibility = View.GONE
       }
    }

    override fun toolBarVisible() {
        binding.apply {
            appBarMain.toolbar.isEnabled =true
            appBarMain.consToolbar.isEnabled =true
           appBarMain.toolbar.visibility = View.VISIBLE
            if (appBarMain.appbar.visibility==View.GONE){
                appBarMain.appbar.visibility = View.VISIBLE
            }


        }
    }

    override fun showLoading() {
        binding.apply {
            appBarMain.contentMain.includeLoading.cons.visibility = View.VISIBLE
            appBarMain.toolbar.visibility = View.GONE
        }
    }

    override fun hideLoading() {
        binding.apply {
            appBarMain.contentMain.includeLoading.cons.visibility = View.GONE
            appBarMain.toolbar.visibility = View.VISIBLE
        }
    }

    override fun getToolbar():Toolbar {
        return binding.appBarMain.toolbar
    }

    override fun toolbarGone() {
       binding.appBarMain.toolbar.visibility = View.GONE
       binding.appBarMain.appbar.visibility = View.GONE
    }

    override fun getConfiguration(configuration: Configuration?) {
        if (configuration!=null){
            onConfigurationChanged(configuration)
        }
    }

    override fun toolbar() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.clouse)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.apply {
            appBarMain.basketText.text = getString(R.string.basket)
            navView.menu.findItem(R.id.nav_home).title = getString(R.string.drawer_item_home)
            navView.menu.findItem(R.id.nav_gallery).title = getString(R.string.drawer_item_settings)
        }
    }

    override fun internetConnection(): LiveData<Boolean> {
       return myInternetConnection
    }

    override fun closeDrawer() {
        binding.drawerLayout.closeDrawers()
    }
}