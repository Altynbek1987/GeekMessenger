package com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.extensions.setOnSingleClickListener
import com.geektechkb.feature_main.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryBottomSheetFragment : BottomSheetDialogFragment() {
    private val binding by viewBinding(FragmentProfileBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.galleryBottomSheet.imBack.setOnSingleClickListener {
            findNavController().navigateUp()
        }
    }
}
