package com.github.compose.waveloading

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Shader
import android.view.View
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.transform
import kotlin.math.roundToInt


@Composable
fun WaveLoading2(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {

    Box(
        modifier
            .fillMaxSize()
            .clipToBounds()
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
                            _bitmap = source
                        }
                    }

                }
            },
            update = { view ->
                // View's been inflated or state read in this block has been updated
                // Add logic here if necessary

                // As selectedItem is read here, AndroidView will recompose
                // whenever the state changes
                // Example of Compose -> View communication
            }
        )

//        Canvas(modifier = Modifier.fillMaxSize()) {
//
//            drawIntoCanvas {
//                it.nativeCanvas.drawRect(Rect(0,0, it.nativeCanvas.width, it.nativeCanvas.height), Paint().apply { color = androidx.compose.ui.graphics.Color.Red }.asFrameworkPaint())
//            }
//        }
//
//

        WaveLoadingInternal(waveHeight = _size.height * wageHeight, bitmap = _bitmap)

    }

}


@Composable
private fun WaveLoadingInternal(waveHeight: Float, bitmap: Bitmap) {

    val transition = rememberInfiniteTransition()

    val mPaint = remember(bitmap) {
        Paint().apply {
            alpha = 0.5f
            shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
    }

    val waves = remember(Unit) {
        listOf(
            Wave(0, 0, 2000, scaleX, scaleY),
            Wave(0, 0, 1500, scaleX, scaleY),
            Wave(0, 0, 1000, scaleX, scaleY)
        )
    }

    val animate0 by transition.animateOf(waves[0].duration)
    val animate1 by transition.animateOf(waves[1].duration)
    val animate2 by transition.animateOf(waves[2].duration)

    val animates = listOf(animate0, animate1, animate2)


    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        drawImage(toGrayscale(bitmap).asImageBitmap())

        drawIntoCanvas { canvas ->

            waves.forEachIndexed { index, wave ->
                wave.updateWavePath(
                    size.width.roundToInt(),
                    size.height.roundToInt(),
                    waveHeight.roundToInt(),
                    mCurProgress
                )

                canvas.withSave {

                    wave.offsetX = wave.width / 2 * (1 - animates[index])

                    canvas.translate(
                        -wave.offsetX,
                        -wave.offsetY
                    )

                    mPaint.shader?.transform {
                        setTranslate(wave.offsetX, 0f)
                    }

                    canvas.drawPath(wave.path.asComposePath(), mPaint)
                }

            }
        }
    }


}

private val cleanPaint = Paint()
