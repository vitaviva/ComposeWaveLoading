package com.github.compose.waveloading

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.ColorMatrix
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt
import android.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope


val mCurProgress = 0.5f
val mStartColor = Color.Red.toArgb()
val mCloseColor = Color.Yellow.toArgb()
val mColorAlpha = 0.8f
val mGradientAngle = 45

val wageHeight = 0.2f   //振幅


val scaleX = 1f
val scaleY = 1f


@Composable
fun InfiniteTransition.animateOf(duration: Int) = animateFloat(
    initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
        animation = tween(duration, easing = CubicBezierEasing(0.4f, 0.2f, 0.6f, 0.8f)),
        repeatMode = RepeatMode.Restart
    )
)


@Deprecated("")
@Composable
fun WaveLoadingBackup(modifier: Modifier) {

    val transition = rememberInfiniteTransition()


    val mMatrix = remember(Unit) {
        Matrix()
    }

    val context = LocalContext.current


    var _size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier
            .onSizeChanged {
                _size = it
            }) {

        val mPaint = remember(_size) {
            Paint().apply {
                alpha = 80
                updateLinearGradient(context, _size.width, _size.height)
            }
        }

        val waves = remember(_size) {
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
            modifier = Modifier
                .matchParentSize()
        ) {

            drawIntoCanvas {
                val canvas = it.nativeCanvas

                waves.forEachIndexed { index, wave ->
                    wave.updateWavePath(
                        canvas.width,
                        canvas.height,
                        (_size.height * wageHeight).roundToInt(),
                        mCurProgress
                    )


                    mMatrix.reset()
                    canvas.save()

                    wave.offsetX = wave.width / 2 * (1 - animates[index])

                    mMatrix.setTranslate(
                        wave.offsetX,
                        0f
                    )
                    canvas.translate(
                        -wave.offsetX,
                        -wave.offsetY
                    )

                    mPaint.shader.setLocalMatrix(mMatrix)
                    canvas.drawPath(wave.path, mPaint)
                    canvas.restore()
                }

            }

        }
    }
}


fun Paint.updateLinearGradient(context: Context, width: Int, height: Int) {
    val startColor = ColorUtils.setAlphaComponent(mStartColor, (mColorAlpha * 255).toInt())
    val closeColor = ColorUtils.setAlphaComponent(mCloseColor, (mColorAlpha * 255).toInt())
    val w = width.toDouble()
    val h: Double = (height * mCurProgress).toDouble()
    val r = Math.sqrt(w * w + h * h) / 2
    val y = r * Math.sin(2 * Math.PI * mGradientAngle / 360)
    val x = r * Math.cos(2 * Math.PI * mGradientAngle / 360)

    if (width == 0 || height == 0) {
        shader = LinearGradient(
            (w / 2 - x).toFloat(),
            (h / 2 - y).toFloat(),
            (w / 2 + x).toFloat(),
            (h / 2 + y).toFloat(),
            startColor,
            closeColor,
            Shader.TileMode.CLAMP
        )
    } else {
        val bm = BitmapFactory.decodeStream(context.resources.assets.open("fundroid.png"))
        val matrix = Matrix()
        matrix.postScale(width / bm.width.toFloat(), height / bm.height.toFloat())
        val new = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
        shader = BitmapShader(new, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bm.recycle()
    }

}


fun toGrayscale(bmpOriginal: Bitmap): Bitmap {
    val width: Int
    val height: Int
    height = bmpOriginal.height
    width = bmpOriginal.width
    val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = android.graphics.Canvas(bmpGrayscale)
    val paint = Paint()
    val cm = ColorMatrix()
    cm.setSaturation(0f)
    val f = ColorMatrixColorFilter(cm)
    paint.colorFilter = f
    c.drawBitmap(bmpOriginal, 0f, 0f, paint)
    return bmpGrayscale
}
