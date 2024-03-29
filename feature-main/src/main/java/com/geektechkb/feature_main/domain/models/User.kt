package com.geektechkb.feature_main.domain.models


import android.os.Parcelable
import com.geektechkb.core.base.BaseDiffModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User(
    val name: String? = null,
    val lastName: String? = null,
    override val phoneNumber: String? = null,
    val profileImage: String? = null,
    val lastSeen: String? = null,
    val isPhoneNumberHidden: Boolean? = null,
) : BaseDiffModel, Parcelable