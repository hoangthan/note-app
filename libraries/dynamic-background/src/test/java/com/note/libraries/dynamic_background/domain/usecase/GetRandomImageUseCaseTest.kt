package com.note.libraries.dynamic_background.domain.usecase

import com.note.libraries.dynamic_background.domain.model.ImageSource
import com.note.libraries.dynamic_background.domain.repository.ImageRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRandomImageUseCaseTest {

    private lateinit var imageRepository: ImageRepository
    private lateinit var getRandomImageUseCase: GetRandomImageUseCase

    @Before
    fun setup() {
        imageRepository = mockk()
        getRandomImageUseCase = GetRandomImageUseCase(imageRepository)
    }

    @Test
    fun `invoke should return image from repository`() = runTest {
        // Given
        val expectedImage = ImageSource.Color("#FFFFFF")
        coEvery { imageRepository.getRandomImage() } returns expectedImage

        // When
        val result = getRandomImageUseCase()

        // Then
        assertEquals(expectedImage, result)
        coVerify { imageRepository.getRandomImage() }
    }
}
