package com.geektechkb.feature_main.presentation.ui.fragments.language

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.data.local.Localization
import com.geektechkb.feature_main.data.local.preferences.LocaleHelper
import com.geektechkb.feature_main.data.local.preferences.PreferencesHelper
import com.geektechkb.feature_main.databinding.FragmentLanguagesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguagesFragment : Fragment(R.layout.fragment_languages) {

    private val binding by viewBinding(FragmentLanguagesBinding::bind)

    @Inject
    lateinit var preferences: PreferencesHelper

    @Inject
    lateinit var localeHelper: LocaleHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineSelectedLanguage()
        setupListener()
    }

    private fun defineSelectedLanguage() {
        binding.radioButtonRussian.isChecked = preferences.language == "ru"
        binding.radioButtonKyrgyz.isChecked = preferences.language == "ky"
    }


    private fun setupListener() {
        listenToLocalizationChanges()
        navigateBack()
    }

    private fun navigateBack() {
        binding.ibBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun listenToLocalizationChanges() {
        setupRussian()
        setupKyrgyz()
    }

    private fun setupRussian() = with(binding.radioButtonRussian) {
        setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setLocale(Localization.RUSSIAN)
                binding.radioButtonKyrgyz.isChecked = false
            }
        }
    }

    private fun setupKyrgyz() = with(binding.radioButtonKyrgyz) {
        setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setLocale(Localization.KYRGYZ)
                binding.radioButtonRussian.isChecked = false
            }
        }
    }

    private fun setLocale(locale: Localization) {
        if (preferences.languageCode != locale.languageCode) {
            preferences.setLocale(locale)
            localeHelper.loadLocale(requireContext())
            requireActivity().recreate()
        }
    }
}