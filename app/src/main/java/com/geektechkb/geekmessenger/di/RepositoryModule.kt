package com.geektechkb.geekmessenger.di

import com.geektechkb.feature_auth.data.repositories.authentication.AuthRepositoryImpl
import com.geektechkb.feature_auth.data.repositories.authentication.CodeVerificationRepositoryImpl
import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import com.geektechkb.feature_auth.domain.repositories.CodeVerificationRepository
import com.geektechkb.feature_main.data.repositories.MessagesRepositoryImpl
import com.geektechkb.feature_main.data.repositories.UsersRepositoryImpl
import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import com.geektechkb.feature_main.domain.repositories.UsersRepository
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

    @Binds
    abstract fun bindMessagesRepository(messagesRepositoryImpl: MessagesRepositoryImpl): MessagesRepository

    @Binds
    abstract fun bindUsersRepository(usersRepositoryImpl: UsersRepositoryImpl): UsersRepository
}