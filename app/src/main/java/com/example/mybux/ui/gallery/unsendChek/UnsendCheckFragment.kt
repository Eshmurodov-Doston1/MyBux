package com.example.mybux.ui.gallery.unsendChek

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.MutableLiveData
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.droidnet.DroidListener
import com.droidnet.DroidNet
import com.example.mybux.R
import com.example.mybux.databinding.FragmentGalleryBinding
import com.example.mybux.databinding.FragmentUnsendCheckBinding
import com.example.mybux.presentation.SetListener
import com.example.mybux.theme.MyAppTheme
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UnsendCheckFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UnsendCheckFragment : ThemeFragment(),DroidListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var myAppTheme: MyAppTheme
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentUnsendCheckBinding? = null
    private val binding get() = _binding!!

    lateinit var droidNet: DroidNet
    lateinit var setListener: SetListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnsendCheckBinding.inflate(inflater,container,false)
        return _binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            droidNet = DroidNet.getInstance()
            droidNet.addInternetConnectivityListener(this@UnsendCheckFragment)
            (activity as AppCompatActivity).supportActionBar?.hide()
            setListener.toolbarGone()
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        myAppTheme = appTheme as MyAppTheme
        binding.cardUnsend.setCardBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
        binding.consUnsend.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
        binding.unsendText.setTextColor(myAppTheme.textColor(requireContext()))
        setListener.hideLoading()
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {

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
         * @return A new instance of fragment UnsendCheckFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UnsendCheckFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}