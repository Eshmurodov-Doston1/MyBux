package com.example.mybux.ui.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.andrognito.pinlockview.PinLockListener
import com.droidnet.DroidNet
import com.example.mybux.App
import com.example.mybux.databinding.*
import com.example.mybux.presentation.SetListener
import com.example.mybux.utils.MybuxResourse
import com.example.mybux.viewModul.ViewModelAuth
import com.example.mybux.viewModul.settings.SettingsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.navigation.fragment.findNavController
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.droidnet.DroidListener
import com.example.domain.language.Language
import com.example.domain.language.LocaleManager
import com.example.mybux.R
import com.example.mybux.theme.MyAppTheme
import com.example.mybux.ui.gallery.listener.LanguageListener
import com.realpacific.clickshrinkeffect.ClickShrinkEffect
import com.realpacific.clickshrinkeffect.applyClickShrink


class GalleryFragment : ThemeFragment(), PinLockListener,DroidListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }
    private var languageListener:LanguageListener?=null
   @Inject
   lateinit var factory:ViewModelProvider.Factory

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var myAppTheme: MyAppTheme
    private lateinit var sharedPreferences:SharedPreferences
    private lateinit var sharedPreferenceMode:SharedPreferences
    private val settingsViewModel:SettingsViewModel by viewModels {factory}
    private val viewModelAuth:ViewModelAuth by viewModels {factory}
    private lateinit var setListener: SetListener
    private lateinit var droidNet: DroidNet
    private lateinit var dialogOpenZReportBinding:DialogOpenZReportBinding
    private lateinit var internetConnection:MutableLiveData<Boolean>
    private var sig:String?=null
    private var code:String?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater,container,false)
        internetConnection = MutableLiveData()
        droidNet = DroidNet.getInstance()
        droidNet.addInternetConnectivityListener(this)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            LocaleManager.setLocale(requireContext())
            sharedPreferences = requireActivity().getSharedPreferences("settingsParol",0)
            sharedPreferenceMode = requireActivity().getSharedPreferences("mode",0)


            val boolean = sharedPreferenceMode.getBoolean("modeApp", false)
            if (boolean){
                val actionBarDrawerToggle = ActionBarDrawerToggle(requireActivity(), setListener.getDrawer(), R.string.app_name, R.string.clouse)
                setListener.getDrawer().addDrawerListener(actionBarDrawerToggle)
                actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
                actionBarDrawerToggle.syncState()
            }

            indicatorDots.pinLength = 6
            pinLockView.attachIndicatorDots(indicatorDots)
            pinLockView.setPinLockListener(this@GalleryFragment)
            setListener.viewGone()
            setListener.setText(requireActivity().getString(R.string.parol))
            val builder1 = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
            val errorDialogBinding1 = builder1.create()

            setListener.toolBarVisible()


            val string = sharedPreferences.getString("password", null)
            if (string!=null){
                binding.indicatorDots.visibility = View.GONE
                binding.pinLockView.visibility = View.GONE
                binding.cons.visibility =View.VISIBLE
                binding.inn.visibility =View.VISIBLE
                viewModelAuth.getAuthDatabase().getAllAuth().observe(viewLifecycleOwner,{
                    if (it.remember_token!=null){
                        (activity as AppCompatActivity).supportActionBar?.title = it.shortname
                    }
                })
            }

            viewModelAuth.getAuthDatabase().getAllAuth().observe(viewLifecycleOwner,{
                if(binding.indicatorDots.visibility == View.GONE){
                    setListener.setText1(it.shortname)
                }
                binding.inn.text = "${requireContext().getString(R.string.inn)}:${it.inn}"
            })

            internetConnection.observe(viewLifecycleOwner,{isConnected->
                if (isConnected){
                    errorDialogBinding1.dismiss()
                    setListener.hideLoading()
                    lifecycleScope.launch {
                        viewModelAuth.getSig().collect { myBuxSig->
                            when(myBuxSig){
                                is MybuxResourse.Loading->{
                                    Log.e("SIG YULDA", "Loading..")
                                }
                                is MybuxResourse.Success->{
                                    Log.e("SIG Keldi", myBuxSig.sig.response?.sig.toString())
                                    sig = myBuxSig.sig.response?.sig.toString()
                                }
                                is MybuxResourse.Error->{
                                    if(myBuxSig.string=="503"){
                                        setListener.showLoading()
                                        val builder1 = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                                        val dialogBinding = DialogNointernetBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                        val errorDialogBinding = builder1.create()
                                        errorDialogBinding.setView(dialogBinding.root)
                                        dialogBinding.textError1.text = "Ok"
                                        dialogBinding.okBtn.setOnClickListener {
                                            errorDialogBinding.dismiss()
                                            setListener.hideLoading()
                                        }
                                        dialogBinding.clearDialogText.text =  getString(R.string.no_internet3)
                                        errorDialogBinding.setCancelable(false)
                                        errorDialogBinding.show()
                                    }else{
                                        val builder = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                                        val create = builder.create()
                                        val errorDialogBinding = ErrorDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                        create.setView(errorDialogBinding.root)
                                        errorDialogBinding.apply {
                                            this.textError.text = myBuxSig.string
                                            this.okBtn.setOnClickListener {
                                                create.dismiss()
                                                setListener.hideLoading()
                                            }
                                            this.okBtn.text = requireContext().getString(R.string.replace)
                                        }
                                        create.setCancelable(false)
                                        create.show()
                                    }
                                }
                            }
                        }
                    }

                    logOut.applyClickShrink()
                    ClickShrinkEffect(logOut)
                    logOut.setOnClickListener {


                        val builder = AlertDialog.Builder(requireContext(),R.style.AppBottomSheetDialogTheme)
                        val create = builder.create()
                        val dialogCustomBinding = DialogCustomBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                        create.setView(dialogCustomBinding.root)
                        dialogCustomBinding.closeButton.setOnClickListener {
                            create.dismiss()
                            setListener.hideLoading()
                        }
                        dialogCustomBinding.logOut.setOnClickListener {
                            viewModelAuth.getAuthDatabase().getAllAuth().observe(viewLifecycleOwner,{
                                lifecycleScope.launch {
                                    if (sig!=null && it.remember_token.isNotEmpty()){
                                        var cookie = "icms[auth]=${it.remember_token}"
                                        viewModelAuth.logUout(cookie, sig.toString()).collect {
                                            when(it){
                                                is MybuxResourse.Loading->{
                                                    setListener.showLoading()
                                                }
                                                is MybuxResourse.SuccessLogOut->{
                                                    setListener.hideLoading()
                                                    viewModelAuth.getAuthDatabase().getAllAuth().observe(viewLifecycleOwner,{
                                                        viewModelAuth.getAuthDatabase().deleteAuth(it)
                                                        create.dismiss()
                                                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.authFragment, false).build()
                                                        setListener.toolbarGone()
                                                        findNavController().navigate(R.id.authFragment,null,navOptions)
                                                    })
                                                }
                                                is MybuxResourse.Error->{
                                                    if (it.string == "503"){
                                                        setListener.showLoading()
                                                        val builder1 = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                                                        val dialogBinding = DialogNointernetBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                                        val errorDialogBinding = builder1.create()
                                                        errorDialogBinding.setView(dialogBinding.root)
                                                        dialogBinding.textError1.text = "Ok"
                                                        dialogBinding.okBtn.setOnClickListener {
                                                            errorDialogBinding.dismiss()
                                                            setListener.hideLoading()
                                                        }
                                                        dialogBinding.clearDialogText.text =  getString(R.string.no_internet3)
                                                        errorDialogBinding.setCancelable(false)
                                                        errorDialogBinding.show()
                                                    }else{
                                                        setListener.showLoading()
                                                        val builder1 = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                                                        val dialogBinding = DialogErrorBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                                        val errorDialogBinding = builder1.create()
                                                        errorDialogBinding.setView(dialogBinding.root)
                                                        dialogBinding.textError1.text = "Ok"
                                                        dialogBinding.okBtn.setOnClickListener {
                                                            errorDialogBinding.dismiss()
                                                            setListener.hideLoading()
                                                        }
                                                        dialogBinding.clearDialogText.text = it.string
                                                        errorDialogBinding.setCancelable(false)
                                                        errorDialogBinding.show()
                                                    }
                                                }
                                                is MybuxResourse.ErrorLogOut->{
                                                    setListener.showLoading()
                                                    val builder1 = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                                                    val dialogBinding = DialogErrorBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                                    val errorDialogBinding = builder1.create()
                                                    errorDialogBinding.setView(dialogBinding.root)
                                                    dialogBinding.textError1.text = "Ok"
                                                    dialogBinding.okBtn.setOnClickListener {
                                                        errorDialogBinding.dismiss()
                                                        setListener.hideLoading()
                                                    }
                                                    dialogBinding.clearDialogText.text =    it.logOut?.error_msg
                                                    errorDialogBinding.setCancelable(false)
                                                    errorDialogBinding.show()
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                        }
                        dialogCustomBinding.dialogCons.setBackgroundColor(myAppTheme.cardBackground(requireContext()))
                        dialogCustomBinding.clearDialogText.setTextColor(myAppTheme.textColor(requireContext()))
                        create.setCancelable(false)
                        create.show()
                    }
                }else
                {
                    setListener.showLoading()
                    val dialogBinding = DialogNointernetBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                    errorDialogBinding1.setView(dialogBinding.root)
                    dialogBinding.okBtn.visibility = View.GONE
                    dialogBinding.clearDialogText.text =  getString(R.string.no_internet3)
                    errorDialogBinding1.setCancelable(false)
                    errorDialogBinding1.show()
                }
            })
            language.applyClickShrink()
            ClickShrinkEffect(language)
            language.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                val create = builder.create()
                val dialogBinding = DialogLanguageBinding.inflate(LayoutInflater.from(context), null, false)
                create.setView(dialogBinding.root)

                dialogBinding.closeButton.setOnClickListener {
                    create.dismiss()
                }

                val language1 = LocaleManager.getLanguage(requireContext())
                when(language1.lowercase()){
                    "uz".lowercase()->{
                       dialogBinding.uz.isChecked = true
                    }
                    "ru".lowercase()->{
                        dialogBinding.ru.isChecked = true
                    }
                    "en".lowercase()->{
                        dialogBinding.cril.isChecked = true
                    }
                }
                dialogBinding.updateLan.setOnClickListener {
                    val id = dialogBinding.radioGroup.checkedRadioButtonId
                    when(id){
                        R.id.uz->{
                            LocaleManager.setNewLocale(context, Language.UZ.value)
                        }
                        R.id.cril->{
                            LocaleManager.setNewLocale(context, Language.EN.value)
                        }
                        R.id.ru->{
                            LocaleManager.setNewLocale(context, Language.RU.value)
                        }
                    }
                    onConfigurationChanged(LocaleManager.configuration)
                    languageListener?.getConfig(LocaleManager.configuration)
                    setListener.getConfiguration(LocaleManager.configuration)
                    create.dismiss()
                }
                dialogBinding.clearDialogText.text = getString(R.string.open_my_z_report)
                var gradientDrawable = dialogBinding.dialogCons.background.mutate() as GradientDrawable
                gradientDrawable.setColor(myAppTheme.cardBackground(requireContext()))
                dialogBinding.clearDialogText.setTextColor(myAppTheme.textColor(requireContext()))
                dialogBinding.radioGroup.setBackgroundColor(myAppTheme.cardBackground(requireContext()))
                dialogBinding.uz.setTextColor(myAppTheme.textColor(requireContext()))
                dialogBinding.ru.setTextColor(myAppTheme.textColor(requireContext()))
                dialogBinding.cril.setTextColor(myAppTheme.textColor(requireContext()))
                create.setCancelable(false)
                create.show()



            }

            aboutFm.applyClickShrink()
            ClickShrinkEffect(aboutFm)
            aboutFm.setOnClickListener {
                setListener.showLoading()
                val fragmentNavigatorExtras = FragmentNavigatorExtras(aboutFm to "consAbout")
                findNavController().navigate(R.id.action_nav_gallery_to_aboutFmFragment,null,null,fragmentNavigatorExtras)
            }

            buttonCashList.applyClickShrink()
            ClickShrinkEffect(buttonCashList)
            buttonCashList.setOnClickListener {
                setListener.showLoading()
                val fragmentNavigatorExtras = FragmentNavigatorExtras(buttonCashList to "cashList")
                findNavController().navigate(R.id.action_nav_gallery_to_cashListFragment,null,null,fragmentNavigatorExtras)
            }


            unsendFragment.applyClickShrink()
            ClickShrinkEffect(unsendFragment)
            unsendFragment.setOnClickListener {
                setListener.showLoading()
                val fragmentNavigatorExtras = FragmentNavigatorExtras(unsendFragment to "unsend")
                findNavController().navigate(R.id.action_nav_gallery_to_unsendCheckFragment,null,null,fragmentNavigatorExtras)
            }

            openZReport.applyClickShrink()
            ClickShrinkEffect(openZReport)
            openZReport.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                val create = alertDialog.create()
                dialogOpenZReportBinding = DialogOpenZReportBinding.inflate(LayoutInflater.from(context), null, false)
                create.setView(dialogOpenZReportBinding.root)
                dialogOpenZReportBinding.nobtn.setOnClickListener {
                    create.dismiss()
                }
                dialogOpenZReportBinding.clearDialogText.text = getString(R.string.open_my_z_report)

                var gradientDrawable = dialogOpenZReportBinding.dialogCons.background.mutate() as GradientDrawable
                gradientDrawable.setColor(myAppTheme.cardBackground(requireContext()))

                dialogOpenZReportBinding.clearDialogText.setTextColor(myAppTheme.textColor(requireContext()))
                create.setCancelable(false)
                create.show()
            }

            clouseZReport.applyClickShrink()
            ClickShrinkEffect(clouseZReport)
            clouseZReport.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                val create = alertDialog.create()
                val dialogOpenZReportBinding = DialogOpenZReportBinding.inflate(LayoutInflater.from(context), null, false)
                dialogOpenZReportBinding.nobtn.setOnClickListener {
                    create.dismiss()
                }
                create.setView(dialogOpenZReportBinding.root)
                dialogOpenZReportBinding.clearDialogText.text = getString(R.string.clouse_my_z_report)
                dialogOpenZReportBinding.clearDialogText.text = getString(R.string.open_my_z_report)
                var gradientDrawable = dialogOpenZReportBinding.dialogCons.background.mutate() as GradientDrawable
                gradientDrawable.setColor(myAppTheme.cardBackground(requireContext()))

                dialogOpenZReportBinding.clearDialogText.setTextColor(myAppTheme.textColor(requireContext()))
                create.setCancelable(false)
                create.show()
            }
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        myAppTheme = appTheme as MyAppTheme
        binding.pinLockView.textColor = myAppTheme.textColor(requireContext())
        setListener.getToolbar().setTitleTextColor(myAppTheme.textColor(requireContext()))
        setListener.getToolbar().setTitleTextColor(myAppTheme.textColor(requireContext()))
        setListener.toolbarTitleColor().setTextColor(myAppTheme.textColor(requireContext()))
        binding.inn.setTextColor(myAppTheme.textColor(requireContext()))
        binding.backIcon.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);


        binding.openZReport.setCardBackgroundColor(myAppTheme.cardBackground(requireContext()))
        binding.createZ.setTextColor(myAppTheme.textColor(requireContext()))
        binding.iconFile.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN)
        binding.backIcon1.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN)


        binding.clouseZReport.setCardBackgroundColor(myAppTheme.cardBackground(requireContext()))
        binding.clouseZ.setTextColor(myAppTheme.textColor(requireContext()))
        binding.iconClouseFile.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);
        binding.backIcon2.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);


        binding.unsendFragment.setCardBackgroundColor(myAppTheme.cardBackground(requireContext()))
        binding.checkApp.setTextColor(myAppTheme.textColor(requireContext()))
        binding.checkIcon.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);
        binding.backIcon3.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);

        binding.buttonCashList.setCardBackgroundColor(myAppTheme.cardBackground(requireContext()))
        binding.cashTxt.setTextColor(myAppTheme.textColor(requireContext()))
        binding.cashImage.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);
        binding.backIcon4.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);

        binding.aboutFm.setCardBackgroundColor(myAppTheme.cardBackground(requireContext()))
        binding.fmTxt.setTextColor(myAppTheme.textColor(requireContext()))
        binding.fmImage.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);
        binding.backIcon4.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);

        binding.language.setCardBackgroundColor(myAppTheme.cardBackground(requireContext()))
        binding.languagEtext.setTextColor(myAppTheme.textColor(requireContext()))
        binding.languageIcon.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);
        binding.backIcon5.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);

        binding.logOut.setCardBackgroundColor(myAppTheme.cardBackground(requireContext()))
        binding.logOutEtext.setTextColor(myAppTheme.textColor(requireContext()))
        binding.logOutIcon.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);
        binding.backIcon6.setColorFilter(myAppTheme.textColor(requireContext()),android.graphics.PorterDuff.Mode.SRC_IN);

    }



    @SuppressLint("SetTextI18n")
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.apply {
            val value = viewModelAuth.getAuthDatabase().getAllAuth().observe(viewLifecycleOwner,{
                inn.text = "${requireContext().getString(R.string.inn)}:${it.inn}"
            })
            createZ.text = getString(R.string.create_z)
            clouseZ.text = getString(R.string.clouse_z)
            checkApp.text = getString(R.string.no_check)
            cashTxt.text = getString(R.string.cash_list)
            fmTxt.text = getString(R.string.about_fm)
            languagEtext.text = getString(R.string.language)
            logOutEtext.text = getString(R.string.logout1)
        }
    }


    override fun onComplete(pin: String?) {
        viewModelAuth.getAuthDatabase().getAllAuth().observe(viewLifecycleOwner,{authEntity->
            lifecycleScope.launch {
                if (sig!=null){
                    if(pin!=null){
                        val cuci = "icms[auth]=${authEntity?.remember_token}"
                        settingsViewModel.sendSettingsAuth(cuci,sig.toString(), authEntity?.phone.toString(),pin.toString()).collect {
                            when(it){
                                is MybuxResourse.Loading->{
                                    setListener.showLoading()
                                }
                                is MybuxResourse.SuccessAuthSettings->{
                                    setListener.hideLoading()
                                    binding.indicatorDots.visibility = View.GONE
                                    binding.pinLockView.visibility = View.GONE
                                    binding.cons.visibility =View.VISIBLE
                                    binding.inn.visibility =View.VISIBLE
                                    binding.inn.text = "${requireContext().getString(R.string.inn)}:${authEntity.inn}"
                                    setListener.setText1(authEntity.shortname)
                                    setListener.setText("")
                                    val string = sharedPreferences.getString("password", null)
                                    val edit = sharedPreferences.edit()
                                    if(string!=null){
                                        edit.clear()
                                        edit.putString("password",pin)
                                        edit.apply()
                                    }else{
                                        edit.putString("password",pin)
                                        edit.apply()
                                    }
                                }
                                is MybuxResourse.Error->{
                                    if (it.string!="503"){
                                        setListener.showLoading()
                                        val builder = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                                        val create = builder.create()
                                        val errorDialogBinding = ErrorDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                        create.setView(errorDialogBinding.root)
                                        errorDialogBinding.apply {
                                            this.textError.text = it.string
                                            this.okBtn.setOnClickListener {
                                                create.dismiss()
                                                setListener.hideLoading()
                                            }
                                            this.okBtn.text = requireContext().getString(R.string.replace)
                                        }
                                        create.setCancelable(false)
                                        create.show()
                                    }
                                }
                            }
                        }
                    }
                }

            }
        })
        binding.pinLockView.resetPinLockView()
    }

    override fun onEmpty() {

    }

    override fun onPinChange(pinLength: Int, intermediatePin: String?) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setListener = activity as SetListener
        if (languageListener!=null){
            languageListener =  activity as LanguageListener
        }
    }


    override fun onDestroy() {
        val edit = sharedPreferences.edit()
        edit.clear()
        edit.apply()
        super.onDestroy()
        _binding= null
       droidNet.removeInternetConnectivityChangeListener(this)
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        internetConnection.postValue(isConnected)
    }







}