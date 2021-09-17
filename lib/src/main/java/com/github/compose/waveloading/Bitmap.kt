import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.media.ThumbnailUtils
import androidx.compose.ui.graphics.toArgb


/**
 * 位图灰度
 */
fun Bitmap.toGrayscale(): Bitmap {
    val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = android.graphics.Canvas(bmpGrayscale)
    val paint = Paint()
    val cm = ColorMatrix()
    cm.setSaturation(0f)
    val f = ColorMatrixColorFilter(cm)
    paint.colorFilter = f
    c.drawBitmap(this, 0f, 0f, paint)
    return bmpGrayscale
}


/**
 * 位图单色化
 */
fun Bitmap.toColor(color: androidx.compose.ui.graphics.Color): Bitmap {
    val bmp = Bitmap.createBitmap(
        width, height, Bitmap.Config.ARGB_8888
    )
    val oldPx = IntArray(width * height) //用来存储原图每个像素点的颜色信息
    getPixels(oldPx, 0, width, 0, 0, width, height) //获取原图中的像素信息

    val newPx = oldPx.map {
        color.copy(Color.alpha(it) / 255f).toArgb()
    }.toTypedArray().toIntArray()
    bmp.setPixels(newPx, 0, width, 0, 0, width, height) //将处理后的像素信息赋给新图
    return bmp
}


/**
 * 转为二值图像
 *
 * @param bmp 原图bitmap
 *
 * @return
 */
fun Bitmap.convertToBMW(tmp: Int = 100): Bitmap {
    val pixels = IntArray(width * height) // 通过位图的大小创建像素点数组
    // 设定二值化的域值，默认值为100
    //tmp = 180;
    getPixels(pixels, 0, width, 0, 0, width, height)
    var alpha = 0xFF shl 24
    for (i in 0 until height) {
        for (j in 0 until width) {
            val grey = pixels[width * i + j]
            // 分离三原色
            alpha = grey and -0x1000000 shr 24
            var red = grey and 0x00FF0000 shr 16
            var green = grey and 0x0000FF00 shr 8
            var blue = grey and 0x000000FF
            if (red > tmp) {
                red = 255
            } else {
                red = 0
            }
            if (blue > tmp) {
                blue = 255
            } else {
                blue = 0
            }
            if (green > tmp) {
                green = 255
            } else {
                green = 0
            }
            pixels[width * i + j] = (alpha shl 24 or (red shl 16) or (green shl 8)
                    or blue)
            if (pixels[width * i + j] == -1) {
                pixels[width * i + j] = -1
            } else {
                pixels[width * i + j] = -16777216
            }
        }
    }
    // 新建图片
    val newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    // 设置图片数据
    newBmp.setPixels(pixels, 0, width, 0, 0, width, height)
    val bitmap = ThumbnailUtils.extractThumbnail(newBmp, width, height)
    return bitmap
}


//抖动算法来对图像进行二值化处理
fun Bitmap.convertGreyImgByFloyd(): Bitmap {
    val pixels = IntArray(width * height) //通过位图的大小创建像素点数组
    getPixels(pixels, 0, width, 0, 0, width, height)
    val gray = IntArray(height * width)
    for (i in 0 until height) {
        for (j in 0 until width) {
            val grey = pixels[width * i + j]
            val red = grey and 0x00FF0000 shr 16
            gray[width * i + j] = red
        }
    }
    var e = 0
    for (i in 0 until height) {
        for (j in 0 until width) {
            val g = gray[width * i + j]
            if (g >= 128) {
                pixels[width * i + j] = (Color.alpha(pixels[width * i + j]) shl 24) or -0x1
                e = g - 255

            } else {
                pixels[width * i + j] = -0x1000000
                e = g - 0
            }
            if (j < width - 1 && i < height - 1) {
                //右边像素处理
                gray[width * i + j + 1] += 3 * e / 8
                //下
                gray[width * (i + 1) + j] += 3 * e / 8
                //右下
                gray[width * (i + 1) + j + 1] += e / 4
            } else if (j == width - 1 && i < height - 1) {//靠右或靠下边的像素的情况
                //下方像素处理
                gray[width * (i + 1) + j] += 3 * e / 8
            } else if (j < width - 1 && i == height - 1) {
                //右边像素处理
                gray[width * i + j + 1] += e / 4
            }
        }
    }
    val mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    mBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return mBitmap
}


/**
 * 图片进行二值化黑白
 */
fun zeroAndOne(bm: Bitmap): Bitmap? {
    val width = bm.width //原图像宽度
    val height = bm.height //原图像高度
    var color: Int //用来存储某个像素点的颜色值
    var r: Int
    var g: Int
    var b: Int
    var a: Int //红，绿，蓝，透明度
    //创建空白图像，宽度等于原图宽度，高度等于原图高度，用ARGB_8888渲染，这个不用了解，这样写就行了
    val bmp = Bitmap.createBitmap(
        width, height, Bitmap.Config.ARGB_8888
    )
    val oldPx = IntArray(width * height) //用来存储原图每个像素点的颜色信息
    val newPx = IntArray(width * height) //用来处理处理之后的每个像素点的颜色信息
    /**
     * 第一个参数oldPix[]:用来接收（存储）bm这个图像中像素点颜色信息的数组
     * 第二个参数offset:oldPix[]数组中第一个接收颜色信息的下标值
     * 第三个参数width:在行之间跳过像素的条目数，必须大于等于图像每行的像素数
     * 第四个参数x:从图像bm中读取的第一个像素的横坐标
     * 第五个参数y:从图像bm中读取的第一个像素的纵坐标
     * 第六个参数width:每行需要读取的像素个数
     * 第七个参数height:需要读取的行总数
     */
    bm.getPixels(oldPx, 0, width, 0, 0, width, height) //获取原图中的像素信息
    for (i in 0 until width * height) { //循环处理图像中每个像素点的颜色值
        color = oldPx[i] //取得某个点的像素值
        r = Color.red(color) //取得此像素点的r(红色)分量
        g = Color.green(color) //取得此像素点的g(绿色)分量
        b = Color.blue(color) //取得此像素点的b(蓝色分量)
        a = Color.alpha(color) //取得此像素点的a通道值

        //此公式将r,g,b运算获得灰度值，经验公式不需要理解
        var gray = (r.toFloat() * 0.3 + g.toFloat() * 0.59 + b.toFloat() * 0.11).toInt()
        //下面前两个if用来做溢出处理，防止灰度公式得到到灰度超出范围（0-255）
        if (gray > 255) {
            gray = 255
        }
        if (gray < 0) {
            gray = 0
        }
        if (gray != 0) { //如果某像素的灰度值不是0(黑色)就将其置为255（白色）
            gray = 255
        }
        newPx[i] = Color.argb(a, gray, gray, gray) //将处理后的透明度（没变），r,g,b分量重新合成颜色值并将其存储在数组中
    }
    /**
     * 第一个参数newPix[]:需要赋给新图像的颜色数组//The colors to write the bitmap
     * 第二个参数offset:newPix[]数组中第一个需要设置给图像颜色的下标值//The index of the first color to read from pixels[]
     * 第三个参数width:在行之间跳过像素的条目数//The number of colors in pixels[] to skip between rows.
     * Normally this value will be the same as the width of the bitmap,but it can be larger(or negative).
     * 第四个参数x:从图像bm中读取的第一个像素的横坐标//The x coordinate of the first pixels to write to in the bitmap.
     * 第五个参数y:从图像bm中读取的第一个像素的纵坐标//The y coordinate of the first pixels to write to in the bitmap.
     * 第六个参数width:每行需要读取的像素个数The number of colors to copy from pixels[] per row.
     * 第七个参数height:需要读取的行总数//The number of rows to write to the bitmap.
     */
    bmp.setPixels(newPx, 0, width, 0, 0, width, height) //将处理后的像素信息赋给新图
    return bmp //返回处理后的图像
}


fun gray2Binary(graymap: Bitmap): Bitmap? {
    //得到图形的宽度和长度
    val width = graymap.width
    val height = graymap.height
    //创建二值化图像
    var binarymap: Bitmap? = null
    binarymap = graymap.copy(Bitmap.Config.ARGB_8888, true)
    //依次循环，对图像的像素进行处理
    for (i in 0 until width) {
        for (j in 0 until height) {
            //得到当前像素的值
            val col = binarymap.getPixel(i, j)
            //得到alpha通道的值
            val alpha = col and -0x1000000
            //得到图像的像素RGB的值
            val red = col and 0x00FF0000 shr 16
            val green = col and 0x0000FF00 shr 8
            val blue = col and 0x000000FF
            // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
            var gray =
                (red.toFloat() * 0.3 + green.toFloat() * 0.59 + blue.toFloat() * 0.11).toInt()
            //对图像进行二值化处理
            gray = if (gray <= 95) {
                0
            } else {
                255
            }
            // 新的ARGB
            val newColor = alpha or (gray shl 16) or (gray shl 8) or gray
            //设置新图像的当前像素值
            binarymap.setPixel(i, j, newColor)
        }
    }
    return binarymap
}