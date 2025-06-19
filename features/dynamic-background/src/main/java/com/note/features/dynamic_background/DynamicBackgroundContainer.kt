package com.note.features.dynamic_background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.note.libraries.dynamic_background.domain.model.ImageSource

@Composable
fun DynamicBackgroundContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val viewModel: DynamicBackgroundViewModel = viewModel()
    val background by viewModel.background.collectAsState()

    val boxModifier = when (val bg = background) {
        is ImageSource.Color -> modifier.background(Color(bg.hexColor.toColorInt()))
        else -> modifier
    }

    Box(modifier = boxModifier) {
        content()
    }
}
