package com.github.compose.waveloading

import android.annotation.SuppressLint
import androidx.annotation.FloatRange
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@SuppressLint("UnnecessaryComposedModifier", "ComposableModifierFactory")
@Composable
fun Modifier.waveLoading(
    foreDrawType: DrawType = DrawType.DrawImage,
    backDrawType: DrawType = rememberDrawColor(color = Color.LightGray),
    @FloatRange(from = 0.0, to = 1.0) progress: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0) amplitude: Float = defaultAmlitude,
    @FloatRange(from = 0.0, to = 1.0) velocity: Float = defaultVelocity,
): Modifier = composed {
    val dp = LocalDensity.current.run { 1.dp.toPx() }

    val waves = remember {
        listOf(
            WaveAnim(waveDuration, 0f, 0f, scaleX, scaleY),
            WaveAnim((waveDuration * 0.75f).roundToInt(), 0f, 0f, scaleX, scaleY),
            WaveAnim((waveDuration * 0.5f).roundToInt(), 0f, 0f, scaleX, scaleY)
        )
    }

    val infiniteTransition = rememberInfiniteTransition()
    val waveAnimes = waves.map {
        infiniteTransition.animateOf(duration = it.duration)
    }

    val background = MaterialTheme.colors.background

    val xfermodeModifier = remember(progress, amplitude, velocity, waves, backDrawType, foreDrawType) {
        DrawXfermodeModifier(
            foreDrawType = foreDrawType,
            backDrawType = if (backDrawType is DrawType.None) DrawType.DrawColor(background) else backDrawType,
            dp = dp,
            progress = progress,
            amplitude = amplitude,
            velocity = velocity,
            waves = waves,
            waveAnimes = waveAnimes
        )
    }
    xfermodeModifier
}

private class DrawXfermodeModifier(
    private val foreDrawType: DrawType,
    private val backDrawType: DrawType,
    private val dp: Float,
    private val progress: Float,
    private val amplitude: Float,
    private val velocity: Float,
    private val waves: List<WaveAnim>,
    private val waveAnimes: List<State<Float>>
) : DrawModifier {

    private val wavesColor = when (foreDrawType) {
        is DrawType.DrawColor -> foreDrawType.color
        else -> Color.LightGray
    }.copy(alpha = foreDrawAlpha)


    /**
     * 因为目前第二个图层是使用的SrcIn，只显示上半部分；
     * 所以第一图层为 进度，第二图层为 背景。
     */
    private val forcePaint = Paint().apply {
        when (foreDrawType) {
            is DrawType.DrawColor -> {
                style = PaintingStyle.Fill
                colorFilter = ColorFilter.tint(foreDrawType.color)
            }
            DrawType.DrawImage,
            DrawType.None -> {
                // nothing to do
            }
        }
    }

    private val backPaint = Paint().apply {
        style = PaintingStyle.Fill

        when (backDrawType) {
            is DrawType.DrawColor -> {
                colorFilter = ColorFilter.tint(backDrawType.color)
            }
            DrawType.None -> {
                //
            }
            DrawType.DrawImage -> {
                val matrix = ColorMatrix()
                matrix.setToSaturation(0f)
                colorFilter = ColorFilter.colorMatrix(matrix)
            }
        }
    }

    override fun ContentDrawScope.draw() = drawIntoCanvas { canvas ->
        canvas.withSaveLayer(maxBounds(), forcePaint) {
            drawContent()
        }

        canvas.withSaveLayer(maxBounds(), backPaint) {
            drawContent()
            drawWaves(wavesColor, blendMode = BlendMode.SrcIn)
        }
    }

    private fun DrawScope.drawWaves(waveColor: Color, blendMode: BlendMode) {
        waves.forEachIndexed { index, wave ->
            val maxWidth = 2 * scaleX * size.width / velocity.coerceAtLeast(0.1f)
            val maxHeight = scaleY * size.height
            val offsetX = maxWidth / 2 * (1 - waveAnimes[index].value) - wave.offsetX
            val offsetY = wave.offsetY
            translate(-offsetX, -offsetY) {
                drawPath(
                    wave.buildWavePath(
                        dp = dp,
                        width = maxWidth,
                        height = maxHeight,
                        amplitude = size.height * amplitude,
                        progress = progress
                    ),
                    color = waveColor,
                    blendMode = blendMode
                )
            }
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun DrawScope.maxBounds() = Rect(0f, 0f, size.width, size.height)
