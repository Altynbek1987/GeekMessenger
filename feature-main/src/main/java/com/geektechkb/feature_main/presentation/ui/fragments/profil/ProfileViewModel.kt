package com.geektechkb.feature_main.presentation.ui.fragments.profil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.geektechkb.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : BaseViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text
}