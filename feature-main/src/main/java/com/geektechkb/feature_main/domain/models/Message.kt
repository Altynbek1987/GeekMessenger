package com.geektechkb.feature_main.domain.models

import com.geektechkb.core.base.BaseDiffModel

data class Message(
    override val id: String,
    val message: String,
    val timeMessageWasSent: Long
) : BaseDiffModel<String>
