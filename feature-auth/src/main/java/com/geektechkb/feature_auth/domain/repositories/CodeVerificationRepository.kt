package com.geektechkb.feature_auth.domain.repositories

interface CodeVerificationRepository {
    fun getVerificationId(): String?
}