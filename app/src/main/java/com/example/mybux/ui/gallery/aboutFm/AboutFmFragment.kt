package com.example.mybux.ui.gallery.aboutFm

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainer
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.domain.helpers.FiscalUtil
import com.example.domain.helpers.InfoModel
import com.example.mybux.R
import com.example.mybux.adapters.fmAdpter.FmAdapter
import com.example.mybux.databinding.FragmentAboutFmBinding
import com.example.mybux.presentation.SetListener
import com.example.mybux.theme.MyAppTheme
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFmFragment : ThemeFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var setListener: SetListener
    lateinit var listFm:LinkedList<InfoModel>
    lateinit var fmAdapter:FmAdapter
    lateinit var sharedPreferenceMode:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private var _binding:FragmentAboutFmBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutFmBinding.inflate(inflater,container,false)
        return _binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.about_fm)
            listFm = LinkedList()
            setListener.hideLoading()
            sharedPreferenceMode = requireActivity().getSharedPreferences("mode",0)
            val boolean = sharedPreferenceMode.getBoolean("modeApp", false)
            if (boolean){
                var upArrow = resources.getDrawable(R.drawable.ic_left_arrow)
                upArrow.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(upArrow)
            }else{
                var upArrow = resources.getDrawable(R.drawable.ic_left_arrow)
                upArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_IN)
                (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(upArrow)
            }
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
        binding.apply {
            cons.setCardBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            rvFm.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            listFm = LinkedList()
            fmAdapter = FmAdapter(myAppTheme,requireContext())
            val info = FiscalUtil("6395002861002861000000079010004490000000000000000020210723541806190000000000000000040302555A2103172558339000")
            setListener.showLoading()
            info.getList()?.forEach {
                listFm.add(InfoModel(it.name,it.value))
            }
            fmAdapter.submitList(listFm)
            rvFm.adapter = fmAdapter
            setListener.hideLoading()
            loading.visibility = View.GONE
        }
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
         * @return A new instance of fragment AboutFmFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutFmFragment().apply {
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