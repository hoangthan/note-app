package com.note.libraries.dynamic_background.domain.model

/**
 * A sealed interface representing different sources of an image.
 *
 * This abstraction allows the domain layer to stay platform-agnostic and
 * flexible to various implementations (local colors, remote URLs, raw bytes...)
 */
sealed interface ImageSource {
    data class Color(val hexColor: String) : ImageSource
}
