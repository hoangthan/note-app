package com.note.features.dynamic_background

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.libraries.dynamic_background.domain.model.ImageSource
import com.note.libraries.dynamic_background.domain.usecase.GetRandomImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DynamicBackgroundViewModel @Inject constructor(
    private val getRandomImageUseCase: GetRandomImageUseCase
) : ViewModel() {

    private val _background = MutableStateFlow<ImageSource?>(null)
    val background = _background.asStateFlow()

    init {
        loadBackground()
    }

    private fun loadBackground() {
        viewModelScope.launch {
            _background.value = getRandomImageUseCase()
        }
    }
}
