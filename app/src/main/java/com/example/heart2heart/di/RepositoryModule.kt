package com.example.heart2heart.di

import com.example.heart2heart.auth.data.repository.AuthRepositoryHiltImpl
import com.example.heart2heart.auth.data.repository.ProfileRepositoryHiltImpl
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.auth.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideAuthRepository(impl: AuthRepositoryHiltImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun provideProfileRepository(impl: ProfileRepositoryHiltImpl): ProfileRepository

}