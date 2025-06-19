package com.note.libraries.note.domain

import com.note.libraries.note.domain.usecase.CreateNoteUseCase
import com.note.libraries.note.domain.usecase.CreateNoteUseCaseImpl
import com.note.libraries.note.domain.usecase.DeleteNoteUseCase
import com.note.libraries.note.domain.usecase.DeleteNoteUseCaseImpl
import com.note.libraries.note.domain.usecase.GetNoteUseCase
import com.note.libraries.note.domain.usecase.GetNoteUseCaseImpl
import com.note.libraries.note.domain.usecase.UpdateNoteUseCase
import com.note.libraries.note.domain.usecase.UpdateNoteUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindCreateNoteUseCase(impl: CreateNoteUseCaseImpl): CreateNoteUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteNoteUseCase(impl: DeleteNoteUseCaseImpl): DeleteNoteUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateNoteUseCase(impl: UpdateNoteUseCaseImpl): UpdateNoteUseCase

    @Binds
    @Singleton
    abstract fun bindGetNoteUseCase(impl: GetNoteUseCaseImpl): GetNoteUseCase
}