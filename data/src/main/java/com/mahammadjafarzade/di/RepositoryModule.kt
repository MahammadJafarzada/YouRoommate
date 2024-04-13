package com.mahammadjafarzade.di

import com.mahammadjafarzade.common.util.MySharedPreferences
import com.mahammadjafarzade.common.util.MySharedPreferencesInterface
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
    abstract fun bindReferences(pref : MySharedPreferences) : MySharedPreferencesInterface
}