package com.github.compose.waveloading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color


sealed interface DrawType {
    object None : DrawType
    object DrawImage : DrawType
    data class DrawColor(val color: Color) : DrawType
}


@Composable
fun rememberDrawColor(color: Color = Color.LightGray) =
    remember(color) { DrawType.DrawColor(color) }