package com.github.compose.waveloading

import androidx.compose.ui.graphics.Path

data class WaveAnim(
    val duration: Int,
    val offsetX: Int,
    val offsetY: Int,
    val scaleX: Float,
    val scaleY: Float,
    val path: Path,

)