package com.example.mybux.ui.discount

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.room.Database
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.data.database.DatabaseHelper
import com.example.domain.database.discount.DiscountEntity
import com.example.mybux.App
import com.example.mybux.R
import com.example.mybux.adapters.discountAdapters.DisCountAdapter
import com.example.mybux.databinding.FragmentDiscountBinding
import com.example.mybux.databinding.ItemDiscountBinding
import com.example.mybux.presentation.SetListener
import com.example.mybux.theme.MyAppTheme
import com.example.mybux.viewModul.ViewModelProduct
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import es.dmoral.toasty.Toasty
import javax.inject.Inject

class DiscountFragment : ThemeFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    @Inject
    lateinit var factory:ViewModelProvider.Factory

    private val viewModel:ViewModelProduct by viewModels {factory}
    private lateinit var setListener: SetListener
    lateinit var  disCountAdapter: DisCountAdapter
    private var _binding:FragmentDiscountBinding?=null
    private val binding get() = _binding!!
    lateinit var database:DatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscountBinding.inflate(inflater,container,false)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setListener.viewGone()
            database = viewModel.getDatabase()
           okBtn.setOnClickListener {
               var percent = discountSumm.text.toString().trim()
               if (percent.toDouble()<100){
                   database.insertDiscount(DiscountEntity(persent = percent.toDouble()))
                   discountSumm.text.clear()
               }else{
                   Toasty.error(requireContext(), getString(R.string.no_perc), Toasty.LENGTH_LONG, true).show()
               }
           }


            database.getAllDisCount().observe(viewLifecycleOwner, {
                disCountAdapter.submitList(it)
                rvDiscount.adapter = disCountAdapter
            })
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
        binding.apply {
            setListener.toolbarGone()
            text.setTextColor(myAppTheme.textColor(requireContext()))
            okBtn.setTextColor(myAppTheme.textColor(requireContext()))
            var gradientDrawable = discountSumm.background.mutate() as GradientDrawable
            gradientDrawable.setColor(myAppTheme.cardBackground(requireContext()))
            discountSumm.setHintTextColor(myAppTheme.hintColor(requireContext()))
            disCountAdapter = DisCountAdapter(requireContext(),myAppTheme,object:DisCountAdapter.OnItemClickListener{
                override fun onItemLongClick(discountEntity: DiscountEntity, position: Int,itemDiscountBinding: ItemDiscountBinding) {
                    if (myAppTheme.cardBackground(requireContext()) == Color.parseColor("#FFFFFFFF")){
                        val popupMenu = PopupMenu(requireContext(), itemDiscountBinding.root)
                        popupMenu.setForceShowIcon(true)
                        popupMenu.menuInflater.inflate(R.menu.my_menu,popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener {
                            when(it.itemId){
                                R.id.update->{
                                    discountSumm.setText(discountEntity.persent.toString())
                                    okBtn.setOnClickListener {
                                        val discount = discountSumm.text.toString().trim()
                                        if (discount.isNotEmpty()){
                                            discountEntity.persent = discount.toDouble()
                                            database.updateDiscount(discountEntity)
                                        }
                                    }
                                }
                                R.id.delete_dis->{
                                    database.deleteDiscount(discountEntity)
                                }
                            }
                            true
                        }
                        popupMenu.show()
                    }else{
                        var wrapper = ContextThemeWrapper(requireContext(), R.style.PopupMenu)
                        val popupMenu = PopupMenu(wrapper, itemDiscountBinding.root)
                        popupMenu.setForceShowIcon(true)
                        popupMenu.menuInflater.inflate(R.menu.my_menu,popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener {
                            when(it.itemId){
                                R.id.update->{
                                    discountSumm.setText(discountEntity.persent.toString())
                                    okBtn.setOnClickListener {
                                        val discount = discountSumm.text.toString().trim()
                                        if (discount.isNotEmpty()){
                                            discountEntity.persent = discount.toDouble()
                                            database.updateDiscount(discountEntity)
                                        }
                                    }
                                }
                                R.id.delete_dis->{
                                    database.deleteDiscount(discountEntity)
                                }
                            }
                            true
                        }
                        popupMenu.menu.findItem(R.id.update).iconTintList = requireActivity().getColorStateList(R.color.white)
                        popupMenu.menu.findItem(R.id.delete_dis).iconTintList = requireActivity().getColorStateList(R.color.white)
                        popupMenu.show()
                    }
                }
            })
            rvDiscount.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            discountSumm.setTextColor(myAppTheme.textColor(requireContext()))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setListener= activity as SetListener
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}