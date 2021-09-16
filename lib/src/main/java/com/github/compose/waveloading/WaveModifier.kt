package com.github.compose.waveloading

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.withSaveLayer


fun Modifier.wave() : Modifier = composed {
    WaveModifier()
}

class WaveModifier() : DrawModifier {

    private val cleanPaint = android.graphics.Paint()

    override fun ContentDrawScope.draw() {

        val bitmap = Bitmap.createBitmap(size.width.toInt(), size.height.toInt(), Bitmap.Config.ARGB_8888)
        drawContext.canvas.nativeCanvas.setBitmap(bitmap)
        drawIntoCanvas {
            it.withSaveLayer(Rect(0f, 0f, size.width, size.height), paint = Paint()) {
                drawContent()
            }
            Log.e("wangp", bitmap.width.toString() + ":"+ bitmap.height )
            val bm = toGrayscale(bitmap)
            it.nativeCanvas.drawBitmap(bm, 0f, 0f, cleanPaint)
        }

    }

}