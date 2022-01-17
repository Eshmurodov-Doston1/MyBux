package com.example.mybux.ui.auth

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.mybux.R
import java.util.ArrayList
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.droidnet.DroidNet
import com.example.domain.bytArray.StrUtil.toByteArray
import com.example.domain.helpers.FiscalUtil
import com.example.domain.helpers.InfoModel
import com.example.domain.helpers.fm.ErrorItem
import com.example.domain.models.auth.reqAuth.ReqAuth
import com.example.mybux.App
import com.example.mybux.databinding.*
import com.example.mybux.message.ErrorMessage
import com.example.mybux.presentation.SetListener
import com.example.mybux.theme.MyAppTheme
import com.example.mybux.utils.MybuxResourse
import com.example.mybux.viewModul.ViewModelAuth
import com.telpo.tps550.api.reader.SmartCardReader
import com.telpo.tps550.api.util.StringUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.yt.ofd.codec.applet.decoder.InfoDecoder
import uz.yt.ofd.codec.exception.IllegalTerminalIDException
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBE
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthFragment : ThemeFragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @Inject
    lateinit var factory:ViewModelProvider.Factory
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    lateinit var sigMy:MutableLiveData<String>
    lateinit var setListener: SetListener
    lateinit var droidNet: DroidNet

    var count = 0
    private val reader by lazy { SmartCardReader(requireContext(), SmartCardReader.SLOT_PSAM1) }

    private val viewModel:ViewModelAuth by viewModels{factory}

    var listTerminalId = MutableLiveData<String>()
    val affineFormats: ArrayList<String> = ArrayList()
    private var fiscalData: List<InfoModel>? = null
    var requestFocus:Boolean = false
    private val TAG = "AuthFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater,container,false)

        return _binding?.root
    }
    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            loginBtn.isEnabled = false
            sigMy = MutableLiveData()
            setListener.toolbarGone()
            setListener.setToolbarGone()
            setListener.hideToolbar()
            requestFocus = phoneInput.requestFocus()

            phoneInput.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if(phoneInput.rawText.length==9){
                        Log.e("phoneNumber", phoneInput.rawText)
                        requestFocus = password.requestFocus()
                    }
                }
            })

            if (requestFocus) {
                val mgr = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                mgr!!.showSoftInput(phoneInput, InputMethodManager.SHOW_IMPLICIT)
            }
            password.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (password.text?.length!!>=6){
                        cons.setBackgroundColor(Color.parseColor("#2C81FB"))
                        textP.setTextColor(Color.WHITE)
                        loginBtn.isEnabled = true
                    }else{
                        cons.setBackgroundColor(Color.parseColor("#F6F6F7"))
                        textP.setTextColor(Color.parseColor("#B5ADAD"))
                        loginBtn.isEnabled = false
                    }
                }

            })

            droidNet= DroidNet.getInstance()
            val builder1 = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
            val errorDialogBinding1 = builder1.create()

            droidNet.addInternetConnectivityListener {internet->
                if (internet){
                    setListener.hideLoading()
                    errorDialogBinding1.dismiss()
                    lifecycleScope.launch {
                        viewModel.getSig().collect { resourse->
                            when(resourse){
                                is MybuxResourse.Loading->{
                                    setListener.showLoading()
                                }
                                is MybuxResourse.Success->{
                                    sigMy.postValue(resourse.sig.response?.sig)
                                    setListener.hideLoading()
                                }
                                is MybuxResourse.Error->{
                                    if (resourse.string.equals("503")){
                                        setListener.showLoading()
                                        val builder1 = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
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
                                        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                                        val create = alertDialog.create()
                                        val dialogBinding = ErrorDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                        create.setView(dialogBinding.root)
                                        dialogBinding.textError.text = resourse.string
                                        if (resourse.string == "503"){
                                            dialogBinding.okBtn.text = "Ok"
                                            dialogBinding.okBtn.setOnClickListener {
                                                create.dismiss()
                                            }
                                        }else{
                                            dialogBinding.okBtn.setText(requireContext().getString(R.string.approval))
                                            dialogBinding.okBtn.setOnClickListener {
                                                create.dismiss()
                                            }
                                        }
                                        create.setCancelable(false)
                                        create.show()
                                    }
                                }
                            }
                        }
                    }

                    loginBtn.setOnClickListener {
                        setListener.showLoading()
                        OpenTask(this@AuthFragment).execute()
                        sigMy.observe(viewLifecycleOwner, {
                            if (it.isNotEmpty()){
                                val device_id = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
                                listTerminalId.observe(viewLifecycleOwner,  { terminalId->
                                    var reqAuth = ReqAuth(it, "998${phoneInput.rawText}", password.text.toString(), "1",device_id,terminalId)
                                    lifecycleScope.launchWhenCreated {
                                        val auth = viewModel.getAuth(reqAuth)
                                        auth.collect {resourse->
                                            when(resourse){
                                                is MybuxResourse.Loading->{
                                                    setListener.showLoading()
                                                    binding.loginBtn.visibility = View.GONE
                                                }
                                                is MybuxResourse.SuccessAuth->{
                                                    resourse.authEntity.observe(viewLifecycleOwner, {authEntity->
                                                        if (authEntity!=null){
                                                            findNavController().navigate(R.id.nav_home)
                                                            setListener.hideLoading()
                                                        }else{
                                                            setListener.showLoading()
                                                            val builder1 = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                                                            val dialogBinding = DialogErrorBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                                            val errorDialogBinding = builder1.create()
                                                            errorDialogBinding.setView(dialogBinding.root)
                                                            dialogBinding.textError1.text = "Ok"
                                                            dialogBinding.okBtn.setOnClickListener {
                                                                errorDialogBinding.dismiss()
                                                                setListener.hideLoading()
                                                            }
                                                            dialogBinding.clearDialogText.text = requireContext().getString(R.string.error_show)
                                                            errorDialogBinding.setCancelable(false)
                                                            errorDialogBinding.show()
                                                        }
                                                    })
                                                }
                                                is MybuxResourse.Error->{
                                                    setListener.hideLoading()
                                                    if(count==0){
                                                        if(resourse.string.equals("503")){
                                                            setListener.showLoading()
                                                            val builder1 = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
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
                                                            val alertDialog = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                                                            val create = alertDialog.create()
                                                            val dialogBinding = DialogErrorBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                                            create.setView(dialogBinding.root)
                                                            dialogBinding.clearDialogText.text = resourse.string
                                                            if (resourse.string.equals("503")){
                                                                dialogBinding.textError1.text = "Ok"
                                                                dialogBinding.okBtn.setOnClickListener {
                                                                    create.dismiss()
                                                                }
                                                            }else{
                                                                dialogBinding.textError1.setText(requireContext().getString(R.string.approval))
                                                                dialogBinding.okBtn.setOnClickListener {
                                                                    create.dismiss()
                                                                }
                                                            }
                                                            create.setCancelable(false)
                                                            create.show()
                                                        }
                                                    }
                                                    binding.loginBtn.visibility = View.VISIBLE
                                                    count++
                                                }
                                            }
                                        }
                                    }
                                })
                            }
                        })
                        count=0
                    }
                }else{
                    setListener.showLoading()
                    val dialogBinding = DialogNointernetBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                    errorDialogBinding1.setView(dialogBinding.root)
                    dialogBinding.textError1.text = "Ok"
                    dialogBinding.okBtn.setOnClickListener {
                        if (internet){
                            errorDialogBinding1.dismiss()
                            setListener.hideLoading()
                        }
                    }
                    dialogBinding.clearDialogText.text =  getString(R.string.no_internet3)
                    errorDialogBinding1.setCancelable(false)
                    errorDialogBinding1.show()
                }
            }


        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
        binding.apply {
            text.setTextColor(myAppTheme.textColor(requireContext()))
            phoneInput.setTextColor(myAppTheme.textColor(requireContext()))
            phoneInput.setHintTextColor(myAppTheme.hintColor(requireContext()))
            infoPhone.setTextColor(myAppTheme.textColor(requireContext()))
            password.setTextColor(myAppTheme.textColor(requireContext()))
            password.setHintTextColor(myAppTheme.hintColor(requireContext()))
            textP.setTextColor(myAppTheme.textColor(requireContext()))
            authCons.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            var gradientDrawable = phoneInput.background.mutate() as GradientDrawable
            gradientDrawable.setColor(myAppTheme.cardBackground(requireContext()))
            var gradientDrawable1 = password.background.mutate() as GradientDrawable
            gradientDrawable1.setColor(myAppTheme.cardBackground(requireContext()))
            cons.setBackgroundColor(myAppTheme.buttonBack(requireContext()))
        }
    }

    class OpenTask(var authFragment: AuthFragment):AsyncTask<Void, Integer, Boolean>(){
        override fun doInBackground(vararg p0: Void?): Boolean {
            val isOpen = authFragment.reader.open()
            Log.e("Reader open", isOpen.toString() + "")
            return isOpen
        }
        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result!!) {
                if (authFragment.reader.iccPowerOn()) {

                    val response: String =
                        authFragment.fmConnect("00 A4 04 00 0E 66697363616C6472697665533031").toString()
                    if (response == "9000") {
                        val fmData: String = authFragment.fmConnect("00 82 00 00").toString()
                        Log.e("FMDATA", fmData)
                        //                        byte[] fmDataByte =fmConnectByte("00 82 00 00");

                        val info = FiscalUtil(fmData)

                        val substring = fmData.substring(fmData.length - 4)
                        if (substring == "9000") {
                            val infoDecoder =
                                InfoDecoder(StringUtil.toBytes(fmData.substring(0, fmData.length - 4)))
                            try {
                                Log.e("InfoDecoder", infoDecoder.decode().terminalID)
                                authFragment.listTerminalId.postValue(infoDecoder.decode().terminalID)
                            } catch (e: IllegalTerminalIDException) {
                                e.printStackTrace()
                                Log.e("INFODECODER", e.message + "Message")
                            }
                            Handler(Looper.getMainLooper()).postDelayed({
                                authFragment.fiscalData = info.getList()
                                Log.e("Fiscal o'qilgandi", "SD$fmData")
                                authFragment.fmConnect("00 A4 04 00 00")
                            }, 1000)
                        } else {
                            val message: ErrorItem = ErrorMessage.getMessage(substring, authFragment.requireContext())
                      //      mainActivityListener.showError(message.value)
                        }
                    } else {
                      //  showError(getString(R.string.no_ficsal))
                        Log.e("Data uspeshno", "Qaytmadi")
                        authFragment.showError(authFragment.getString(R.string.no_ficsal))
                        authFragment.binding.loginBtn.visibility = View.GONE
                    }
                } else {
                    Log.e(authFragment.TAG, "Reader ochishda xatolik")
                }
            } else {
                authFragment.showError(authFragment.getString(R.string.neCreate))
                authFragment.binding.loginBtn.visibility = View.GONE
                //showText(getString(), TextEnum.error)
            }

        }


    }



    fun showError(str:String){
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
        val create = alertDialog.create()
        val dialogBinding = ErrorDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        create.setView(dialogBinding.root)
        dialogBinding.textError.text = str
        dialogBinding.okBtn.text = getString(R.string.replace)
        dialogBinding.okBtn.setOnClickListener {
            setListener.hideLoading()
            binding.loginBtn.visibility = View.VISIBLE
            create.dismiss()
        }
        create.setCancelable(false)
        create.show()
    }


    fun fmConnect(apduStr: String): String? {
        var result: ByteArray?=null
        val pRevAPDULen = IntArray(1)
        pRevAPDULen[0] = 300
        val pSendAPDU: ByteArray = toByteArray(apduStr)
        if (pSendAPDU!=null){
            result = reader.transmit(pSendAPDU)
        }
        return StringUtil.toHexString(result)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setListener = activity as SetListener
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)

                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}