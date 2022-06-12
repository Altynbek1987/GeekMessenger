package com.geektechkb.feature_main.domain.models

import com.geektechkb.core.base.BaseDiffModel

data class SentMessage(
    override val id: String,
    val message: String,
    val timeMessageWasSent: Long
) : BaseDiffModel<String>
