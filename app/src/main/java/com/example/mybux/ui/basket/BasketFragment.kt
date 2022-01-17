package com.example.mybux.ui.basket

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.droidnet.DroidNet
import com.example.data.database.DatabaseHelper
import com.example.domain.database.basket.Basket
import com.example.domain.database.discount.DiscountEntity
import com.example.domain.helpers.numberFromat.NumberFormats
import com.example.mybux.App
import com.example.mybux.R
import com.example.mybux.adapters.basketAdapters.BasketAdapter
import com.example.mybux.adapters.discountAdapters.DisCountAdapter
import com.example.mybux.adapters.discountAdapters.DisCountAdapterBasket
import com.example.mybux.databinding.*
import com.example.mybux.presentation.SetListener
import com.example.mybux.theme.MyAppTheme
import com.example.mybux.utils.errorDialog
import com.example.mybux.viewModul.ViewModelProduct
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.String
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject


class BasketFragment : ThemeFragment() {
    @Inject
    lateinit var factory:ViewModelProvider.Factory
    private val viewModel:ViewModelProduct by viewModels{factory}
    lateinit var mDroidNet: DroidNet
    lateinit var basketAdapter: BasketAdapter
    lateinit var setListener: SetListener
    var allPriceSumm:BigInteger = BigInteger.valueOf(0)
    var internetConnection:MutableLiveData<Boolean> = MutableLiveData()
    private var _binding:FragmentBasketBinding?=null
    private val binding get() = _binding!!
    lateinit var database:DatabaseHelper
    lateinit var sharedPreferenceMode:SharedPreferences
    lateinit var myAppTheme: MyAppTheme
    lateinit var disCountAdapter: DisCountAdapterBasket
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasketBinding.inflate(inflater,container,false)

        return _binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setListener.setToolbarGone()
            sharedPreferenceMode = requireActivity().getSharedPreferences("mode",0)
            val boolean = sharedPreferenceMode.getBoolean("modeApp", false)
            setListener.closeDrawer()


            if (boolean){
                var upArrow = resources.getDrawable(R.drawable.ic_left_arrow)
                upArrow.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(upArrow)
            }else{
                var upArrow = resources.getDrawable(R.drawable.ic_left_arrow)
                upArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_IN)
                (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(upArrow)
            }



            database = viewModel.getDatabase()
            myBasketClear.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                val create = builder.create()
                val dialogBinding = ClearDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)

                var gradientDrawable = dialogBinding.dialogCons.background.mutate() as GradientDrawable
                gradientDrawable.setColor(myAppTheme.ActivityBackgroundColor(requireContext()))

                dialogBinding.clearDialogText.setTextColor(myAppTheme.textColor(requireContext()))
                create.setView(dialogBinding.root)
                dialogBinding.apply {
                    clearButton.setOnClickListener {
                       database.clearBasket()
                        create.dismiss()
                        findNavController().popBackStack()
                    }
                    closeButton.setOnClickListener {
                        create.dismiss()
                    }
                }
                create.setCancelable(false)
                create.show()
            }

            var allPriceDi = BigDecimal.ZERO
            database.getBasketListMy().forEach {
                val integerDi = it.price!!.divide(BigInteger.valueOf(100))
                allPriceDi = allPriceDi.add(BigDecimal(integerDi.multiply(it.count)))
            }
            allPriceSumm = BigInteger.valueOf(allPriceDi.multiply(BigDecimal.valueOf(100)).longValueExact())
            binding.allprice.text = getString(R.string.payment_text) + " " + NumberFormats.parseNumberFormat(allPriceDi.toDouble()) + " " + getString(R.string.price)




            database.getBasketList().observe(viewLifecycleOwner,{
                basketAdapter.submitList(it)
                rvBasket.adapter = basketAdapter
            })
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        myAppTheme = appTheme as MyAppTheme
        binding.apply {

            sharedPreferenceMode = requireActivity().getSharedPreferences("mode",0)
            val boolean = sharedPreferenceMode.getBoolean("modeApp", false)
            setListener.internetConnection().observe(viewLifecycleOwner,{
                lifecycleScope.launch(Dispatchers.Main) {
                    if (it && !boolean){
                        clearIcon.setImageResource(R.drawable.ic_delete_outline)
                        textClearBasket.setTextColor(requireActivity().getColor(R.color.clear_text))
                        myBasketClear.setBackgroundColor(Color.WHITE)
                        binding.noInternetIcon.visibility = View.GONE
                    }else if(!it && !boolean){
                        binding.noInternetIcon.visibility = View.VISIBLE
                        binding.noInternetIcon.setImageResource(R.drawable.ic_vector_5)
                        myBasketClear.setBackgroundColor(Color.RED)
                    }else if (!it && boolean){
                        binding.noInternetIcon.visibility = View.VISIBLE
                        binding.noInternetIcon.setImageResource(R.drawable.ic_vector_night)
                        clearIcon.setImageResource(R.drawable.ic_delete_no_internet)
                        myBasketClear.setBackgroundColor(Color.RED)
                    }else if (it && boolean){
                        myBasketClear.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
                        textClearBasket.setTextColor(myAppTheme.clearBasketText(requireContext()))
                        clearIcon.setImageResource(R.drawable.ic_delete_no_internet)
                        binding.noInternetIcon.visibility = View.GONE
                    }else{
                        clearIcon.setImageResource(R.drawable.ic_delete_no_internet)
                        textClearBasket.setTextColor(Color.WHITE)
                        myBasketClear.setBackgroundColor(Color.RED)
                        binding.noInternetIcon.visibility = View.VISIBLE
                    }
                }
            })



            consBasket.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            myBasketClear.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            textClearBasket.setTextColor(myAppTheme.clearBasketText(requireContext()))
            clearIcon.setColorFilter(myAppTheme.clearBasketIconColor(requireContext()),PorterDuff.Mode.SRC_IN)

            basketAdapter = BasketAdapter(myAppTheme,requireContext(),object:BasketAdapter.OnItemClickListener{
                @SuppressLint("SetTextI18n")
                override fun onItemClick(basket: Basket, position: Int) {
                    val alertDialog = AlertDialog.Builder(requireContext(), R.style.AppBottomSheetDialogTheme)
                    val create = alertDialog.create()
                    val dialogBasketItemBinding = DialogBasketItemBinding.inflate(LayoutInflater.from(requireContext()), null, false)

                    var gradientDrawable = dialogBasketItemBinding.consBasket.background.mutate() as GradientDrawable
                    gradientDrawable.setColor(myAppTheme.ActivityBackgroundColor(requireContext()))

                    dialogBasketItemBinding.name.setTextColor(myAppTheme.textColor(requireContext()))
                    dialogBasketItemBinding.summ.setTextColor(myAppTheme.textColor(requireContext()))
                    dialogBasketItemBinding.discountName.setTextColor(myAppTheme.textColor(requireContext()))
                    dialogBasketItemBinding.clouse.setTextColor(myAppTheme.textColor(requireContext()))
                    dialogBasketItemBinding.clouse1.setTextColor(myAppTheme.textColor(requireContext()))
                    dialogBasketItemBinding.imageBottom.setColorFilter(myAppTheme.iconsColor(requireContext()),PorterDuff.Mode.SRC_IN)
                    var gradientDrawableBack =  dialogBasketItemBinding.discountBack.background.mutate() as GradientDrawable
                    gradientDrawableBack.setColor(myAppTheme.spinnerColor(requireContext()))
                    val boolean = sharedPreferenceMode.getBoolean("modeApp", false)
                    if (boolean){
                        clearIcon.setImageResource(R.drawable.ic_delete_no_internet)
                    }else{
                        clearIcon.setImageResource(R.drawable.ic_delete_outline)
                    }
                    dialogBasketItemBinding.apply {
                        name.text = basket.name
                        val generator = ColorGenerator.MATERIAL
                        val color2 = generator.getColor(basket.name)
                        val drawable = TextDrawable.builder().buildRect(String.valueOf(basket.name?.get(0)), color2)
                        image.setImageDrawable(drawable)
                        val integer: BigInteger = basket.price?.divide(BigInteger.valueOf(100))?:BigInteger.ZERO
                        var allPrice = BigDecimal(integer.multiply(basket.count))
                        summ.text = Html.fromHtml("<b>" + NumberFormats.parseNumberFormat(allPrice.toDouble()).toString() + " " + getString(R.string.price) + "</b> • (" + NumberFormats.parseNumberFormat(integer.toDouble()).toString() + " " + getString(R.string.price) + ") × " + basket.count.toString() + " " + basket.measure_name, Html.FROM_HTML_MODE_COMPACT)
                        dialogBasketItemBinding.clouse1.setOnClickListener {
                            create.dismiss()
                        }

                        dialogBasketItemBinding.clouse.setOnClickListener {
                            create.dismiss()
                        }
                        plus.setOnClickListener {
                            basket.count = basket.count?.add(BigInteger.ONE)
                            val integerDialog: BigInteger = basket.price!!.divide(BigInteger.valueOf(100))
                            val allPriceDialog = BigDecimal(integerDialog.multiply(basket.count))
                            summ.text = Html.fromHtml("<b>" + NumberFormats.parseNumberFormat(allPriceDialog.toDouble()).toString() + " " + getString(R.string.price) + "</b> • (" + NumberFormats.parseNumberFormat(integer.toDouble()).toString() + " " + getString(R.string.price) + ") × " + basket.count.toString() + " " + basket.measure_name, Html.FROM_HTML_MODE_COMPACT)
                            database.updateBasket(basket)
                            var allPriceDi = BigDecimal.ZERO
                            database.getBasketListMy().forEach {
                                val integerDi = it.price!!.divide(BigInteger.valueOf(100))
                                allPriceDi = allPriceDi.add(BigDecimal(integerDi.multiply(it.count)))
                            }
                            allPriceSumm = BigInteger.valueOf(allPriceDi.multiply(BigDecimal.valueOf(100)).longValueExact())
                            binding.allprice.text = getString(R.string.payment_text) + " " + NumberFormats.parseNumberFormat(allPriceDi.toDouble()) + " " + getString(R.string.price)
                        }
                        minus.setOnClickListener {
                            if (basket.count?.toInt()?:0>=1){
                                basket.count = basket.count?.subtract(BigInteger.ONE)
                                val integerDialog: BigInteger = basket.price!!.divide(BigInteger.valueOf(100))
                                val allPriceDialog = BigDecimal(integerDialog.multiply(basket.count))
                                summ.text = Html.fromHtml("<b>" + NumberFormats.parseNumberFormat(allPriceDialog.toDouble()).toString() + " " + getString(R.string.price) + "</b> • (" + NumberFormats.parseNumberFormat(integer.toDouble()).toString() + " " + getString(R.string.price) + ") × " + basket.count.toString() + " " + basket.measure_name, Html.FROM_HTML_MODE_COMPACT)
                                database.updateBasket(basket)
                                var allPriceDi = BigDecimal.ZERO
                                database.getBasketListMy().forEach {
                                    val integerDi = it.price!!.divide(BigInteger.valueOf(100))
                                    allPriceDi = allPriceDi.add(BigDecimal(integerDi.multiply(it.count)))
                                }
                                allPriceSumm = BigInteger.valueOf(allPriceDi.multiply(BigDecimal.valueOf(100)).longValueExact())
                                binding.allprice.text = getString(R.string.payment_text) + " " + NumberFormats.parseNumberFormat(allPriceDi.toDouble()) + " " + getString(R.string.price)
                            }
                        }
                        delete.setOnClickListener {
                            database.getBasketList().observe(viewLifecycleOwner,{
                                if (it.isNotEmpty()){
                                    database.deleteBasket(basket)
                                    var allPriceDi = BigDecimal.ZERO
                                    database.getBasketListMy().forEach {
                                        val integerDi = it.price!!.divide(BigInteger.valueOf(100))
                                        allPriceDi = allPriceDi.add(BigDecimal(integerDi.multiply(it.count)))
                                    }
                                    allPriceSumm = BigInteger.valueOf(allPriceDi.multiply(BigDecimal.valueOf(100)).longValueExact())
                                    binding.allprice.text = getString(R.string.payment_text) + " " + NumberFormats.parseNumberFormat(allPriceDi.toDouble()) + " " + getString(R.string.price)
                                }else{
                                    database.deleteBasket(basket)
                                    findNavController().popBackStack()
                                }
                            })
                            create.dismiss()
                        }
                        discountBack.setOnClickListener {
                            database.getAllDisCount().observe(viewLifecycleOwner,{
                                if (it.isNotEmpty()){
                                    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
                                    val dialogBinding = DiscountDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                                    disCountAdapter = DisCountAdapterBasket(requireContext(),myAppTheme,object:DisCountAdapterBasket.OnItemClickListener{
                                        override fun onItemClick(
                                            discountEntity: DiscountEntity,
                                            position: Int,
                                            itemDiscountBinding: ItemDiscount1Binding
                                        ) {
                                            var count = basket.count_up
                                            var price = basket.price
                                            dialogBasketItemBinding.discountName.text = "${discountEntity.persent} %"

                                            val priceBasket = basket.price?.divide(BigInteger.valueOf(100))
                                            val priceDiscountSumm = priceBasket?.subtract(priceBasket?.multiply(BigInteger.valueOf(discountEntity.persent.toLong()))?.divide(BigInteger.valueOf(100)))

                                            val priceDiscount = priceDiscountSumm?.multiply(basket.count)
                                            basket.discount = BigInteger.valueOf(discountEntity.persent.toLong())
                                            dialogBasketItemBinding.summ.text = Html.fromHtml("<b>" + NumberFormats.parseNumberFormat(priceDiscount?.toDouble()!!).toString() + " " + getString(R.string.price) + "</b> • (" + NumberFormats.parseNumberFormat(priceDiscountSumm.toDouble()).toString() + " " + getString(R.string.price) + ") × " + basket.count.toString() + " " + basket.measure_name, Html.FROM_HTML_MODE_COMPACT)
                                            count++
                                            basket.count_up = count
                                            if (count == 1) {
                                                basket.price = priceDiscountSumm.multiply(BigInteger.valueOf(100))
                                                basket.perValue =price?: BigInteger.ZERO
                                            }
                                            database.updateBasket(basket)
                                            bottomSheetDialog.dismiss()
                                        }
                                    })
                                    database.getAllDisCount().observe(viewLifecycleOwner,{
                                        disCountAdapter.submitList(it)
                                        dialogBinding.rvDialog.adapter = disCountAdapter
                                    })
                                    val gradientDrawable1 = dialogBinding.consBottom.background.mutate() as GradientDrawable
                                    gradientDrawable1.setColor(myAppTheme.ActivityBackgroundColor(requireContext()))
                                    bottomSheetDialog.setContentView(dialogBinding.root)
                                    bottomSheetDialog.show()
                                }else{
                                    view?.errorDialog(requireContext(),getString(R.string.no_discount),"Ok",myAppTheme)
                                }
                            })
                        }
                    }
                    create.setView(dialogBasketItemBinding.root)
                    create.setCancelable(false)
                    create.show()
                }
            })
            allSumm.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)

                //bottomSheetDialog.setContentView()

                bottomSheetDialog.show()
            }
         }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setListener = activity as  SetListener
    }

}