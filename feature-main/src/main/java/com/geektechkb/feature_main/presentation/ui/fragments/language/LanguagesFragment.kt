package com.geektechkb.feature_main.presentation.ui.fragments.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.data.local.Localization
import com.geektechkb.feature_main.data.local.preferences.PreferencesHelper
import com.geektechkb.feature_main.databinding.FragmentLanguagesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguagesFragment : Fragment() {

    private lateinit var binding: FragmentLanguagesBinding

    @Inject
    lateinit var preferences: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.radioButtonRussian.isChecked = preferences.getLanguage() == "ru"
        binding.radioButtonEnglish.isChecked = preferences.getLanguage() == "en"
        binding.radioButtonKyrgyz.isChecked = preferences.getLanguage() == "ky"
        setupRussian()
        setupListener()
    }


    private fun setupRussian() = with(binding) {
        radioButtonEnglish.setOnCheckedChangeListener { compoundButton, b ->
            setLocale(Localization.RUSSIAN)
            radioButtonEnglish.isChecked = false

        }
        binding.radioButtonEnglish.setOnCheckedChangeListener { chip, isChecked ->
            radioButtonEnglish.isChecked = true
            if (isChecked) {
                setLocale(Localization.ENGLISH)
                binding.radioButtonRussian.isChecked = false
                binding.radioButtonKyrgyz.isChecked = false
            }
            if (!isChecked) {
                radioButtonEnglish.isChecked = false

            }
        }

        binding.radioButtonKyrgyz.setOnCheckedChangeListener { _, isChecked ->
            radioButtonKyrgyz.isChecked = true
            if (isChecked) {
                setLocale(Localization.KYRGYZ)
                binding.radioButtonEnglish.isChecked = false
                binding.radioButtonRussian.isChecked = false
            }
            if (!isChecked) {
                radioButtonKyrgyz.isChecked = false

            }
        }

        binding.radioButtonRussian.setOnCheckedChangeListener { chip, isChecked ->
            radioButtonRussian.isChecked = true
            if (isChecked) {
                setLocale(Localization.RUSSIAN)
                radioButtonEnglish.isChecked = false
                radioButtonKyrgyz.isChecked = false
            }
            if (!isChecked) {
                radioButtonRussian.isChecked = false
            }
        }
    }

    private fun setLocale(locale: Localization) {
        if (preferences.getLanguageCode() != locale.languageCode) {
            preferences.setLocale(locale)
            activity?.recreate()
        }
    }

    private fun setupListener() {
        binding.checkByn.setOnClickListener {
            findNavController().navigate(R.id.action_languagesFragment_to_profileFragment)
        }
        binding.ibBack.setOnClickListener {
            findNavController().navigate(R.id.action_languagesFragment_to_profileFragment)
        }
    }
}