package com.geektechkb.geekmessenger.di

import com.geektechkb.feature_auth.data.repositories.authentication.AuthRepositoryImpl
import com.geektechkb.feature_auth.data.repositories.authentication.CodeVerificationRepositoryImpl
import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import com.geektechkb.feature_auth.domain.repositories.CodeVerificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindCodeVerificationRepository(codeVerificationRepositoryImpl: CodeVerificationRepositoryImpl): CodeVerificationRepository
}