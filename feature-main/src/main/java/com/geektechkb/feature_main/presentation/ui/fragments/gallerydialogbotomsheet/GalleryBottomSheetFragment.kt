package com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet

import android.os.Bundle
import com.geektechkb.feature_main.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.root
    }
}
