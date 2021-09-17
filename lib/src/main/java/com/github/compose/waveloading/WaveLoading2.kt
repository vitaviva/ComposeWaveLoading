package com.github.compose.waveloading

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Shader
import android.view.View
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.transform
import convertGreyImgByFloyd
import convertToBMW
import gray2Binary
import toColor
import toGrayscale
import zeroAndOne


@Composable
fun WaveLoading2(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {

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
                                (source.width  - _size.width) / 2,
                                (source.height  - _size.height) / 2,
                                _size.width ,
                                _size.height
                            )
                            source.recycle()
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


        with(LocalDensity.current) {
            Box(
                Modifier
                    .width(_size.width.toDp())
                    .height(_size.height.toDp())
                    .align(Alignment.Center)
                    .clipToBounds()
            ) {
                //一个dp在当前设备表示的像素量（水波的绘制精度设为一个dp单位）
                WaveLoadingInternal(1.dp.toPx(), bitmap = _bitmap)
            }
        }

    }

}


@Composable
private fun WaveLoadingInternal(dp: Float, bitmap: Bitmap) {


    val transition = rememberInfiniteTransition()

    val mPaint = remember(bitmap) {
        Paint().apply {
            alpha = 0.5f
            shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
    }

    val waves = remember(Unit) {
        listOf(
            WaveAnim(2000, 0f, 0f, scaleX, scaleY),
            WaveAnim(1500, 0f, 0f, scaleX, scaleY),
            WaveAnim(1000, 0f, 0f, scaleX, scaleY)
        )
    }

    val animates = waves.map { transition.animateOf(duration = it.duration) }
    val grayBitmap = remember(bitmap) {
        bitmap.toColor(Color.LightGray)
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        drawImage(grayBitmap!!.asImageBitmap())

        drawIntoCanvas { canvas ->

            waves.forEachIndexed { index, wave ->

                canvas.withSave {

                    val maxWidth = 2 * scaleX * size.width
                    val maxHeight = scaleY * size.height
                    val offsetX = maxWidth / 2 * (1 - animates[index].value) - wave.offsetX
                    val offsetY = wave.offsetY

                    canvas.translate(
                        -offsetX,
                        -offsetY
                    )

                    mPaint.shader?.transform {
                        setTranslate(offsetX, 0f)
                    }

                    canvas.drawPath(
                        wave.buildWavePath(
                            dp = dp,
                            width = maxWidth,
                            height = maxHeight,
                            waveHeight = size.height * wageHeight,
                            progress = mCurProgress
                        ), mPaint
                    )
                }

            }
        }
    }

}

