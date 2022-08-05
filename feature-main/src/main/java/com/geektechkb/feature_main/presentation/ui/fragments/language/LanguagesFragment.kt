package com.geektechkb.feature_main.presentation.ui.fragments.language

import Localization
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.geektechkb.core.data.local.preferences.PreferencesHelper
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.data.local.Localization
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
        binding.checkboxRus.isChecked = preferences.getLanguage() == "ru"
        setupRussian()
        setupListener()
    }


    private fun setupRussian() = with(binding) {
        checkboxEng.setOnCheckedChangeListener { compoundButton, b ->
            setLocale(Localization.RUSSIAN)
            checkboxEng.isChecked = false

        }
        binding.checkboxEng.setOnCheckedChangeListener { chip, isChecked ->
            checkboxEng.isChecked = true
            if (isChecked) {
                setLocale(Localization.ENGLISH)
                binding.checkboxRus.isChecked = false
                binding.checkboxKg.isChecked = false
            }
            if (!isChecked) {
                checkboxEng.isChecked = false

            }
        }

        binding.checkboxKg.setOnCheckedChangeListener { chip, isChecked ->
            checkboxKg.isChecked = true
            if (isChecked) {
                setLocale(Localization.KYRGYZ)
                binding.checkboxEng.isChecked = false
                binding.checkboxRus.isChecked = false
            }
            if (!isChecked) {
                checkboxKg.isChecked = false

            }
        }

        binding.checkboxRus.setOnCheckedChangeListener { chip, isChecked ->
            checkboxRus.isChecked = true
            if (isChecked) {
                setLocale(Localization.RUSSIAN)
                binding.checkboxEng.isChecked = false
                binding.checkboxKg.isChecked = false
            }
            if (!isChecked) {
                checkboxRus.isChecked = false
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