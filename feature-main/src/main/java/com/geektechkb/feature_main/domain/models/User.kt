package com.geektechkb.feature_main.domain.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geektechkb.core.base.BaseDiffModel

@Entity
data class User(
    val name: String? = null,
    val lastName: String? = null,
    @PrimaryKey(autoGenerate = true)
    override val phoneNumber: String? = null,
    val profileImage: String? = null,
    val lastSeen: String? = null,
) : BaseDiffModel


