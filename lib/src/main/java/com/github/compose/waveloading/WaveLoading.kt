package com.github.compose.waveloading

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Shader
import android.view.View
import androidx.annotation.FloatRange
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.transform
import toColor
import toGrayscale
import kotlin.math.roundToInt

private const val defaultAmlitude = 0.2f
private const val defaultVelocity = 1.0f
private const val waveDuration = 2000
private const val frontDrawAlpha = 0.5f
private val alphaBitmap by lazy {
    Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8)
}

data class WaveConfig(
    val progress: Float,
    val frontDrawType: DrawType,
    val backDrawType: DrawType,
    @FloatRange(from = 0.0, to = 1.0) val amplitude: Float,
    @FloatRange(from = 0.0, to = 1.0) val velocity: Float
)

val LocalWave = compositionLocalOf<WaveConfig> {
    error("No Local WaveConfig")
}

@Composable
fun WaveLoading(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    drawType: DrawType = DrawType.DrawImage,
    backDrawType: DrawType = rememberDrawColor(color = Color.LightGray),
    @FloatRange(from = 0.0, to = 1.0) amplitude: Float = defaultAmlitude,
    @FloatRange(from = 0.0, to = 1.0) velocity: Float = defaultVelocity,
    content: @Composable BoxScope.() -> Unit
) {


    Box(
        modifier.fillMaxSize()
    ) {

        var _size by remember { mutableStateOf(IntSize.Zero) }

        var _bitmap by remember {
            mutableStateOf(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565))
        }
        AndroidView(
            modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
            factory = { context ->
                // Creates custom view
                object : AbstractComposeView(context) {

                    @Composable
                    override fun Content() {
                        Box(
                            Modifier
                                .wrapContentSize()
                                .onSizeChanged {
                                    _size = it
                                }) {

                            content()
                        }
                    }

                    override fun drawChild(
                        canvas: Canvas?,
                        child: View?,
                        drawingTime: Long
                    ): Boolean {
                        val source = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        val canvas2 = Canvas(source)
                        return super.drawChild(canvas2, child, drawingTime).also {
                            _bitmap = Bitmap.createBitmap(
                                source,
                                (source.width - _size.width) / 2,
                                (source.height - _size.height) / 2,
                                _size.width,
                                _size.height
                            )
                            source.recycle()
                        }
                    }

                }
            }

        )


        CompositionLocalProvider(
            LocalWave provides WaveConfig(progress, drawType, backDrawType, amplitude, velocity)
        ) {
            with(LocalDensity.current) {
                Box(
                    Modifier
                        .width(_size.width.toDp())
                        .height(_size.height.toDp())
                        .align(Alignment.Center)
                        .clipToBounds()
                ) {
                    WaveLoadingInternal(bitmap = _bitmap)
                }
            }
        }

    }

}


@Composable
private fun WaveLoadingInternal(bitmap: Bitmap) {

    val dp = LocalDensity.current.run {
        1.dp.toPx() //一个dp在当前设备表示的像素量（水波的绘制精度设为一个dp单位）
    }

    val transition = rememberInfiniteTransition()

    val (progress, frontDrawType, backDrawType, amplitude, velocity) = LocalWave.current

    val frontPaint = remember(frontDrawType, bitmap) {
        Paint().apply {
            alpha = frontDrawAlpha
            shader = BitmapShader(
                when (frontDrawType) {
                    is DrawType.DrawColor -> bitmap.toColor(frontDrawType.color)
                    is DrawType.DrawImage -> bitmap
                    else -> alphaBitmap
                },
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
            )
        }
    }

    val backPaint = remember(backDrawType, bitmap) {
        Paint().apply {
            shader = BitmapShader(
                when (backDrawType) {
                    is DrawType.DrawColor -> bitmap.toColor(backDrawType.color)
                    is DrawType.DrawImage -> bitmap.toGrayscale()
                    else -> alphaBitmap
                },
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
            )
        }
    }

    val waves = remember(Unit) {
        listOf(
            WaveAnim(waveDuration, 0f, 0f, scaleX, scaleY),
            WaveAnim((waveDuration * 0.75f).roundToInt(), 0f, 0f, scaleX, scaleY),
            WaveAnim((waveDuration * 0.5f).roundToInt(), 0f, 0f, scaleX, scaleY)
        )
    }

    val animates = waves.map { transition.animateOf(duration = it.duration) }


    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        drawIntoCanvas { canvas ->

            //draw back image
            canvas.drawRect(0f, 0f, size.width, size.height, backPaint)

            waves.forEachIndexed { index, wave ->

                canvas.withSave {

                    val maxWidth = 2 * scaleX * size.width / velocity.coerceAtLeast(0.1f)
                    val maxHeight = scaleY * size.height
                    val offsetX = maxWidth / 2 * (1 - animates[index].value) - wave.offsetX
                    val offsetY = wave.offsetY

                    canvas.translate(
                        -offsetX,
                        -offsetY
                    )

                    frontPaint.shader?.transform {
                        setTranslate(offsetX, 0f)
                    }

                    canvas.drawPath(
                        wave.buildWavePath(
                            dp = dp,
                            width = maxWidth,
                            height = maxHeight,
                            amplitude = size.height * amplitude,
                            progress = progress
                        ), frontPaint
                    )
                }

            }
        }
    }

}

