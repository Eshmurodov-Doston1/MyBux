package com.example.mybux.ui.gallery.cashFragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.lifecycleScope
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.droidnet.DroidListener
import com.droidnet.DroidNet
import com.example.mybux.App
import com.example.mybux.R
import com.example.mybux.databinding.FragmentCashListBinding
import com.example.mybux.presentation.SetListener
import com.example.mybux.theme.MyAppTheme
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CashListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CashListFragment : ThemeFragment(),DroidListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var droidNet: DroidNet
    lateinit var setListener: SetListener
    lateinit var myAppTheme: MyAppTheme
   private var _binding:FragmentCashListBinding?=null
    private val binding get() = _binding!!
    lateinit var sharedPreferenceMode:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCashListBinding.inflate(inflater,container,false)
        return _binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            sharedPreferenceMode = requireActivity().getSharedPreferences("mode",0)
            val boolean = sharedPreferenceMode.getBoolean("modeApp", false)

            droidNet = DroidNet.getInstance()
            droidNet.addInternetConnectivityListener(this@CashListFragment)

            if (boolean){
                var upArrow = resources.getDrawable(R.drawable.ic_left_arrow);
                upArrow.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(upArrow)

//                val actionBarDrawerToggle = ActionBarDrawerToggle(requireActivity(), setListener.getDrawer(), R.string.app_name, R.string.clouse)
//                actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
//                actionBarDrawerToggle.syncState()
            }
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.cash_page_title)
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        myAppTheme = appTheme as MyAppTheme
        binding.apply {
            cardCash.setCardBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            clearCashText.setTextColor(myAppTheme.textColor(requireContext()))
            setListener.hideLoading()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setListener = activity as SetListener

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        droidNet.removeInternetConnectivityChangeListener(this)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CashListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CashListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        Toast.makeText(requireContext(), "$isConnected", Toast.LENGTH_SHORT).show()
    }
}