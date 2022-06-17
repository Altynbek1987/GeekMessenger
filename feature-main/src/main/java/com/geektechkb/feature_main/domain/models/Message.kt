package com.geektechkb.feature_main.domain.models

import com.geektechkb.core.base.BaseDiffModel

data class Message(
    override val phoneNumber: String? = " ",
    val receiverPhoneNumber: String? = null,
    val message: String? = null,
    val timeMessageWasSent: String? = null
) : BaseDiffModel
