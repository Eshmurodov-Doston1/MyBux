package com.example.mybux.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.domain.language.Language
import com.example.domain.language.LocaleManager
import com.example.mybux.App
import com.example.mybux.R
import com.example.mybux.databinding.FragmentAuthBinding
import com.example.mybux.databinding.FragmentAuthLanguageBinding
import com.example.mybux.databinding.FragmentHomeBinding
import com.example.mybux.theme.MyAppTheme
import com.example.mybux.viewModul.ViewModelAuth
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthLanguageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthLanguageFragment : ThemeFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    @Inject
    lateinit var factory:ViewModelProvider.Factory
    private val viewModel:ViewModelAuth by viewModels{factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    private var _binding: FragmentAuthLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthLanguageBinding.inflate(inflater,container,false)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            LocaleManager.setLocale(requireContext())
            consLoading.visibility = View.VISIBLE
            (activity as AppCompatActivity).supportActionBar?.hide()

            (activity as AppCompatActivity).window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            viewModel.getAuthDatabase().getAllAuth().observe(viewLifecycleOwner, {
                if (it!=null){
                    include.cons.visibility = View.VISIBLE
                    consLoading.visibility = View.VISIBLE
                    findNavController().navigate(R.id.nav_home)
                }else{
                    include.cons.visibility = View.GONE
                    consLoading.visibility = View.GONE
                    uz.setOnClickListener {
                        LocaleManager.setNewLocale(requireContext(), Language.UZ.value)
                        findNavController().navigate(R.id.action_authLanguageFragment3_to_authFragment2)
                    }
                    cril.setOnClickListener {
                        LocaleManager.setNewLocale(requireContext(),
                            Language.EN.value)
                        findNavController().navigate(R.id.action_authLanguageFragment3_to_authFragment2)
                    }
                    ru.setOnClickListener {
                        LocaleManager.setNewLocale(requireContext(),
                            Language.RU.value)
                        findNavController().navigate(R.id.action_authLanguageFragment3_to_authFragment2)
                    }
                }
            })
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
        binding.apply {
            binding.cons.setBackgroundColor(myAppTheme.ActivityBackgroundColor(requireContext()))
            binding.uz.setCardBackgroundColor(myAppTheme.buttonLangColor(requireContext()))
            binding.ru.setCardBackgroundColor(myAppTheme.buttonLangColor(requireContext()))
            binding.cril.setCardBackgroundColor(myAppTheme.buttonLangColor(requireContext()))
            binding.lanText.setTextColor(myAppTheme.textColor(requireContext()))
            binding.lanTextCr.setTextColor(myAppTheme.textColor(requireContext()))
            binding.lanTextRu.setTextColor(myAppTheme.textColor(requireContext()))
            (activity as AppCompatActivity).window.statusBarColor= myAppTheme.statusBarColor(requireContext())
            if (myAppTheme.statusBarColor(requireContext())==resources.getColor(R.color.statusBarColorDay)){
                (activity as AppCompatActivity).window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }else{
                (activity as AppCompatActivity).window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthLanguageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthLanguageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}