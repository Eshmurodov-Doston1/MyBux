package com.example.mybux.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.droidnet.DroidListener
import com.droidnet.DroidNet
import com.example.domain.database.basket.Basket
import com.example.domain.database.products.Products
import com.example.domain.helpers.numberFromat.NumberFormats
import com.example.mybux.App
import com.example.mybux.R
import com.example.mybux.adapters.mainAdapters.MainListAdapter
import com.example.mybux.databinding.FragmentHomeBinding
import com.example.mybux.presentation.SetListener
import com.example.mybux.theme.MyAppTheme
import com.example.mybux.ui.gallery.listener.LanguageListener
import com.example.mybux.utils.MybuxResourse
import com.example.mybux.viewModul.ViewModelAuth
import com.example.mybux.viewModul.ViewModelProduct
import com.matrixxun.starry.badgetextview.MaterialBadgeTextView
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.String
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import javax.inject.Inject
import android.graphics.PorterDuff

import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.drawable.Drawable
import android.view.*
import androidx.core.content.ContextCompat
import com.realpacific.clickshrinkeffect.ClickShrinkEffect
import com.realpacific.clickshrinkeffect.applyClickShrink


class HomeFragment : ThemeFragment(),LanguageListener{
    lateinit var mDroidNet: DroidNet
    lateinit var setListener:SetListener


    lateinit var myAppTheme:MyAppTheme
    lateinit var liveMyAppTheme:MutableLiveData<MyAppTheme>
    @Inject
    lateinit var factory:ViewModelProvider.Factory

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val viewModelProducts:ViewModelProduct by viewModels { factory }

    private val viewModelAuth:ViewModelAuth by viewModels { factory }

    lateinit var mainListAdapter: MainListAdapter
    private  val TAG = "HomeFragment"

    lateinit var sharedPreferenceMode:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return _binding?.root
    }
    @SuppressLint("StringFormatInvalid", "CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.inject(this)
        setListener.toolBarVisible()
        sharedPreferenceMode = requireActivity().getSharedPreferences("mode",0)
        val boolean = sharedPreferenceMode.getBoolean("modeApp", false)





        if (boolean){
            val actionBarDrawerToggle = ActionBarDrawerToggle(requireActivity(), setListener.getDrawer(), R.string.app_name, R.string.clouse)
            setListener.getDrawer().addDrawerListener(actionBarDrawerToggle)
            actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
            actionBarDrawerToggle.syncState()
        }



        val myAppTheme1 = setListener.myAppTheme()
        setListener.toolbar()

        binding.apply {

            val database = viewModelProducts.getDatabase()
            setListener.viewVisible()
            setListener.toolBarVisible()
            setListener.setText1Visible("")
            database.getBasketList().observe(viewLifecycleOwner,{
                if(it.isNotEmpty()){
                    var allPrice = BigDecimal.ZERO
                    for (b in it) {
                        val integer = b.price!!.divide(BigInteger.valueOf(100))
                        allPrice = allPrice.add(BigDecimal(integer.multiply(b.count)))
                    }
                    allPriceText.text = String.format(getString(R.string.main_all_pay), NumberFormats.parseNumberFormat(allPrice.toDouble()))
                    requireActivity().findViewById<MaterialBadgeTextView>(R.id.active_count).visibility = View.VISIBLE
                }else{
                    allPriceText.text = getString(R.string.main_all_pay1)
                    requireActivity().findViewById<MaterialBadgeTextView>(R.id.active_count).visibility = View.GONE
                }
            })



            addButton.setOnClickListener {
                if (myAppTheme.cardBackground(requireContext()).equals(Color.parseColor("#FFFFFFFF"))){
                    val popupMenu = PopupMenu(requireContext(), addButton)
                    popupMenu.setForceShowIcon(true)
                    popupMenu.menuInflater.inflate(R.menu.add_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                        if (item.itemId == R.id.add_product) {

                        } else if (item.itemId == R.id.add_discount) {
                            findNavController().navigate(R.id.action_nav_home_to_discountFragment)
                            setListener.setToolbar()
                        }
                        true
                    }
                    popupMenu.show()
                }else{
                    var wrapper = ContextThemeWrapper(requireContext(), R.style.PopupMenu)
                    val popupMenu = PopupMenu(wrapper, addButton)
                    popupMenu.setForceShowIcon(true)
                    popupMenu.menuInflater.inflate(R.menu.add_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                        if (item.itemId == R.id.add_product) {

                        } else if (item.itemId == R.id.add_discount) {
                            findNavController().navigate(R.id.action_nav_home_to_discountFragment)
                            setListener.setToolbar()
                        }
                        true
                    }
                    popupMenu.menu.findItem(R.id.add_product).iconTintList = requireActivity().getColorStateList(R.color.white)
                    popupMenu.menu.findItem(R.id.add_discount).iconTintList = requireActivity().getColorStateList(R.color.white)
                    popupMenu.show()
                }
            }






            binding.search.addTextChangedListener {
                renderFilter(it.toString())
            }

            price.applyClickShrink()
            ClickShrinkEffect(price)
            price.setOnClickListener {click->
                val basketListMy = database.getBasketListMy()
                if (basketListMy.isNotEmpty()){
                    findNavController().navigate(R.id.basketFragment)
                   setListener.setToolbar()
                }else{
                    Toasty.error(requireContext(), "Savatda saqlangan mahsulotlar mavjud emas", Toasty.LENGTH_LONG, true).show()
                }
            }

            database.getBasketList().observe(viewLifecycleOwner,{
                if(it.isNotEmpty()){
                    setListener.viewVisible()
                }else {
                    setListener.oneVisible()
                }
            })
        }
    }




    private fun renderFilter(filter:kotlin.String) {
        val storageDataList = ArrayList<Products>()
        val database = viewModelProducts.getDatabase()
        database.getAllProduct().observe(viewLifecycleOwner,{
            for (storage in it) {
                if (storage.bar_code != null && storage.name != null) {
                    if (storage.name!!.lowercase(Locale.getDefault())
                            .contains(filter.lowercase(Locale.getDefault())) || storage.bar_code!!.lowercase(Locale.getDefault())
                            .contains(filter.lowercase(Locale.getDefault())) || storage.cat_title?.lowercase(Locale.getDefault())!!
                            .contains(filter.lowercase(Locale.getDefault()))) {
                        storageDataList.add(storage)
                    }
                }
            }
            if(storageDataList.isNotEmpty() && filter.isNotEmpty()) {
                mainListAdapter.filter(storageDataList)
            }else{
                mainListAdapter.submitList(it)
            }
        })
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        setListener = activity as SetListener
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getConfig(configuration: Configuration?) {
        if (configuration!=null){
            binding.apply {
                val database = viewModelProducts.getDatabase()
                setListener.viewVisible()
                setListener.toolBarVisible()
                setListener.setText1Visible("")
                database.getBasketList().observe(viewLifecycleOwner,{
                    if(it.isNotEmpty()){
                        var allPrice = BigDecimal.ZERO
                        for (b in it) {
                            val integer = b.price!!.divide(BigInteger.valueOf(100))
                            allPrice = allPrice.add(BigDecimal(integer.multiply(b.count)))
                        }
                        allPriceText.text = String.format(getString(R.string.main_all_pay), NumberFormats.parseNumberFormat(allPrice.toDouble()))
                        requireActivity().findViewById<MaterialBadgeTextView>(R.id.active_count).visibility = View.VISIBLE
                    }else{
                        allPriceText.text = getString(R.string.main_all_pay1)
                        requireActivity().findViewById<MaterialBadgeTextView>(R.id.active_count).visibility = View.GONE
                    }
                })
            }
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        liveMyAppTheme = MutableLiveData()
        myAppTheme = appTheme as MyAppTheme
        binding.apply {
            sharedPreferenceMode = requireActivity().getSharedPreferences("mode",0)
            val boolean = sharedPreferenceMode.getBoolean("modeApp", false)
            setListener.internetConnection().observe(viewLifecycleOwner,{
                lifecycleScope.launch(Dispatchers.Main) {
                    if (it && !boolean){
                        binding.noInternet.visibility = View.GONE
                        binding.noInternetIcon.visibility = View.GONE
                    }else if(!it && !boolean){
                        binding.noInternetIcon.visibility = View.VISIBLE
                        binding.noInternetIcon.setImageResource(R.drawable.ic_vector_5)
                        binding.noInternet.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed({
                            GlobalScope.launch(Dispatchers.Main) {
                                binding.noInternet.visibility = View.GONE
                            }
                        },2000)
                    }else if (!it && boolean){
                        binding.noInternetIcon.visibility = View.VISIBLE
                        binding.noInternetIcon.setImageResource(R.drawable.ic_vector_night)
                        binding.noInternet.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed({
                            GlobalScope.launch(Dispatchers.Main) {
                                binding.noInternet.visibility = View.GONE
                            }
                        },2000)
                    }else if (it && boolean){
                        binding.noInternet.visibility = View.GONE
                        binding.noInternetIcon.visibility = View.GONE
                    }else{
                        binding.noInternetIcon.visibility = View.VISIBLE
                        binding.noInternet.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed({
                            GlobalScope.launch(Dispatchers.Main) {
                                binding.noInternet.visibility = View.GONE
                            }
                        },2000)
                    }
                }
            })





            liveMyAppTheme.postValue(myAppTheme)
            consHome.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            var gradientDrawable = search.background.mutate() as GradientDrawable
            gradientDrawable.setColor(myAppTheme.backSearchView(requireContext()))
            search.setHintTextColor(appTheme.textHintColor(requireContext()))
            search.setTextColor(appTheme.textColor(requireContext()))
            var drawable: Drawable? = resources.getDrawable(R.drawable.ic_search_2) //Your drawable image
            drawable = DrawableCompat.wrap(drawable!!)
            DrawableCompat.setTint(drawable,myAppTheme.iconsColor(requireContext())) // Set whatever color you want
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
            search.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            consAdd.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            consQr.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            imageQr.setColorFilter(myAppTheme.iconsColor(requireContext()),PorterDuff.Mode.SRC_IN)
            imageAdd.setColorFilter(myAppTheme.iconsColor(requireContext()),PorterDuff.Mode.SRC_IN)
            val database = viewModelProducts.getDatabase()
            binding.price.setBackgroundColor(myAppTheme.priceBtnColor(requireContext()))
            liveMyAppTheme.observe(viewLifecycleOwner,{
                mainListAdapter = MainListAdapter(requireContext(),it,object: MainListAdapter.OnItemClickListener{
                    override fun OnItemClick(products: Products, position: Int) {
                        var basket = database.findIdBasket(products.id.toLong())
                        if(basket==null){
                            basket = Basket()
                            basket.id = products.id.toLong()
                            basket.name = products.name
                            basket.barCode = products.bar_code
                            basket.classCode = products.classCode
                            basket.label = products.label
                            basket.price = products.price?.let { it1 -> BigInteger.valueOf(it1.toLong()) }
                            if (products.discount != null) {
                                basket.discount = BigInteger.valueOf(products.discount!!.toLong())
                            }
                            if (products.vat != null) {
                                basket.vat = BigInteger.valueOf(products.vat!!.toLong())
                            }
                            if (products.count?.toInt()!=0){
                                basket.count = products.count?.toLong()?.let { BigInteger.valueOf(it) }
                            }else{
                                basket.count = BigInteger.ONE
                            }
                            basket.groupId = products.group_id?.toDouble()
                            basket.measure_name = products.measure_name
                            basket.cat_title = products.cat_title
                            basket.photo = products.photo
                            basket.measure_id = products.measure_id?.toLong()
                            basket.commissionTin = products.commissionTin
                            database.insertBasket(basket)
                        }else{
                            val nas: BigInteger = basket.count!!.add(BigInteger.ONE)
                            database.updateCount(basket.id!!, nas.toString())
                        }
                    }
                })
            })
            mainRv.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))

            viewModelAuth.getAuthDatabase().getAllAuth().observe(viewLifecycleOwner, {
                val cuci = "icms[auth]=${it.remember_token}"
                lifecycleScope.launch {
                    viewModelProducts.getAllProduct(cuci).collect {resourse->
                        when(resourse){
                            is MybuxResourse.Loading->{
                                include.cons.visibility = View.VISIBLE
                            }
                            is MybuxResourse.SuccessGetProduct->{
                                resourse.listProduct.observe(viewLifecycleOwner, {
                                    mainListAdapter.submitList(it)
                                    mainRv.adapter = mainListAdapter
                                })
                                include.cons.visibility = View.GONE
                            }
                            is MybuxResourse.Error->{

                                include.cons.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            })
        }
    }



}