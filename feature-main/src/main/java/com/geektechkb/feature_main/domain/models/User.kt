package com.geektechkb.feature_main.domain.models


import com.geektechkb.core.base.BaseDiffModel
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String? = null,
    val lastName: String? = null,
    override val phoneNumber: String? = null,
    var profileImage: String? = null,
    val lastSeen: String? = null,
    val isPhoneNumberHidden: Boolean? = null,
) : BaseDiffModel