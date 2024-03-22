package com.kong.transmart.di

import android.content.Context
import com.kong.transmart.data.worker.WorkHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WorkModule {
    @Singleton
    @Provides
    fun provideWorkHandler(@ApplicationContext context: Context): WorkHandler {
        return WorkHandler(context)
    }
}