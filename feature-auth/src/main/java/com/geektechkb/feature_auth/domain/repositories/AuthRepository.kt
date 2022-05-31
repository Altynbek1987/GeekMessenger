package com.geektechkb.feature_auth.domain.repositories

interface AuthRepository {

    fun isUserAuthenticated(): Boolean
}