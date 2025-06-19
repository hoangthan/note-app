package com.note.libraries.dynamic_background.data.repository

import com.note.libraries.dynamic_background.domain.model.ImageSource
import com.note.libraries.dynamic_background.domain.repository.ImageRepository
import javax.inject.Inject

internal class LocalImageRepository @Inject constructor() : ImageRepository {
    private val colors = listOf(
        "#FFFFFF",
        "#74D3AE",
        "#678D58",
        "#A6C48A",
        "#F6E7CB",
        "#DD9787",
        "#B3CBB9",
        "#EFD9CE",
        "#FFBCBC",
        "#C1CAD6",
        "#FFE6E6",
        "#C4D6B0",
        "#F7D9C4",
        "#DFD3C3",
        "#D3BCC0",
        "#F1E3D3",
        "#9CADCE",
        "#C5D7BD",
        "#FCD5CE",
        "#E1F0DA",
        "#F2D7D9"
    )

    override suspend fun getRandomImage(): ImageSource {
        return ImageSource.Color(colors.random())
    }
}
