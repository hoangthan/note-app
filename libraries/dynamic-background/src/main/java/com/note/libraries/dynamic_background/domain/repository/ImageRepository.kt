package com.note.libraries.dynamic_background.domain.repository

import com.note.libraries.dynamic_background.domain.model.ImageSource

interface ImageRepository {
    suspend fun getRandomImage(): ImageSource
}
