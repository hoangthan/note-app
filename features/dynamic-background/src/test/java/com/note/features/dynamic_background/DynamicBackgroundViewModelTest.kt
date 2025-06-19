package com.note.features.dynamic_background

import app.cash.turbine.test
import com.note.libraries.dynamic_background.domain.model.ImageSource
import com.note.libraries.dynamic_background.domain.usecase.GetRandomImageUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DynamicBackgroundViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getRandomImageUseCase: GetRandomImageUseCase
    private lateinit var viewModel: DynamicBackgroundViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getRandomImageUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadBackground should update background state with usecase result`() = runTest {
        // Given
        val expectedImageSource = ImageSource.Color("#FFFFFF")
        coEvery { getRandomImageUseCase() } returns expectedImageSource

        // When - init is called in the constructor
        viewModel = DynamicBackgroundViewModel(getRandomImageUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.background.test {
            val emission = awaitItem()
            assertEquals(expectedImageSource, emission)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
