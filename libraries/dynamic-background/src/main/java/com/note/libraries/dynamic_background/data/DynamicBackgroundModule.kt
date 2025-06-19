package com.note.libraries.dynamic_background.data

import com.note.libraries.dynamic_background.data.repository.LocalImageRepository
import com.note.libraries.dynamic_background.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DynamicBackgroundModule {

    @Singleton
    @Binds
    abstract fun bindRepository(impl: LocalImageRepository): ImageRepository
}
