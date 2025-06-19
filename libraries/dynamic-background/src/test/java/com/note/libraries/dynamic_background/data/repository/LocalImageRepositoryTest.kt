package com.note.libraries.dynamic_background.data.repository

import com.note.libraries.dynamic_background.domain.model.ImageSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocalImageRepositoryTest {

    private lateinit var repository: LocalImageRepository

    @Before
    fun setup() {
        repository = LocalImageRepository()
    }

    @Test
    fun `getRandomImage should return a color from predefined list`() = runTest {
        // When
        val result = repository.getRandomImage()

        // Then
        assertTrue(result is ImageSource.Color)
        
        val colorHex = (result as ImageSource.Color).hexColor
        assertTrue(colorHex.startsWith("#"))
        assertTrue(colorHex.length == 7) // Format #RRGGBB
    }
    
    @Test
    fun `getRandomImage should return different colors on multiple calls`() = runTest {
        val results = mutableSetOf<String>()
        
        // When
        repeat(10) {
            val result = repository.getRandomImage()
            results.add((result as ImageSource.Color).hexColor)
        }
        
        // Then
        assertTrue(results.size > 1)
    }
}
