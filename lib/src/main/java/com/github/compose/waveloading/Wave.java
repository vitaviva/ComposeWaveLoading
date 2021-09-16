package com.github.compose.waveloading;

import android.content.res.Resources;
import android.graphics.Path;
import android.util.TypedValue;

/**
 * 水波对象
 * Created by SCWANG on 2017/12/11.
 */
@SuppressWarnings("WeakerAccess")
class Wave /*extends View*/ {

    Path path;          //水波路径
    int width;          //画布宽度（2倍波长）
    int wave;           //波幅（振幅）
    float offsetX;        //水波的水平偏移量
    float offsetY;        //水波的竖直偏移量
    int duration;       //水波移动速度（像素/秒）
    private float scaleX;       //水平拉伸比例
    private float scaleY;       //竖直拉伸比例
    private int curWave;
//    int startColor;     //开始颜色
//    int closeColor;     //结束颜色
//    float alpha;        //颜色透明度

    /**
     * 通过参数构造一个水波对象
     *
     * @param offsetX 水平偏移量
     * @param offsetY 竖直偏移量
     * @param scaleX  水平拉伸量
     * @param scaleY  竖直拉伸量
     * @param wave    波幅（波宽度）
     */
//    @SuppressWarnings("PointlessArithmeticExpression")
    Wave(/*Context context, */int offsetX, int offsetY, int duration, float scaleX, float scaleY) {
//        super(context);
        this.scaleX = scaleX;       //水平拉伸量
        this.scaleY = scaleY;       //竖直拉伸量
        this.offsetX = offsetX;     //水平偏移量
        this.offsetY = offsetY;     //竖直偏移量
        this.duration = duration;   //移动速度（像素/秒）
        this.path = new Path();
    }

//    /*
//     * 根据 波长度、中轴线高度、波幅 绘制水波路径
//     */
//    public Wave(Context context) {
//        this(context, null, 0);
//    }
//
//    public Wave(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public Wave(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Wave);
//
////        startColor = ta.getColor(R.styleable.Wave_mwhStartColor, 0);
////        closeColor = ta.getColor(R.styleable.Wave_mwhCloseColor, 0);
////        alpha = ta.getFloat(R.styleable.Wave_mwhColorAlpha, 0f);
//        scaleX = ta.getFloat(R.styleable.Wave_mwScaleX, 1);
//        scaleY = ta.getFloat(R.styleable.Wave_mwScaleY, 1);
//        offsetX = ta.getDimensionPixelOffset(R.styleable.Wave_mwOffsetX, 0);
//        offsetY = ta.getDimensionPixelOffset(R.styleable.Wave_mwOffsetY, 0);
//        velocity = ta.getDimensionPixelOffset(R.styleable.Wave_mwVelocity, Util.dp2px(10));
//        wave = ta.getDimensionPixelOffset(R.styleable.Wave_mwWaveHeight, 0) / 2;
//
//        ta.recycle();
//    }

    void updateWavePath(int w, int h, int waveHeight, float progress) {
        this.wave = waveHeight;
        this.width = (int) (2 * scaleX * w);  //画布宽度（2倍波长）
        this.path = buildWavePath(width, h, progress);
    }

    void updateWavePath(int w, int h, float progress) {
        int wave = (int) (scaleY * this.wave);//计算拉伸之后的波幅
        float maxWave = h * Math.max(0, (1 - progress));
        if (wave > maxWave) {
            wave = (int) maxWave;
        }

        if (curWave != wave) {
            this.width = (int) (2 * scaleX * w);  //画布宽度（2倍波长）
            this.path = buildWavePath(width, h,  progress);
        }
    }

    protected Path buildWavePath(int width, int height, float progress) {
        int DP = dp2px(1);//一个dp在当前设备表示的像素量（水波的绘制精度设为一个dp单位）
        if (DP < 1) {
            DP = 1;
        }

        int wave = (int) (scaleY * this.wave);//计算拉伸之后的波幅

        //调整振幅，振幅不大于剩余空间
        float maxWave = height * Math.max(0, 1 - progress);
        if (wave > maxWave) {
            wave = (int) maxWave;
        }

        this.curWave = wave;

//        Path path = new Path();
        path.reset();

        path.moveTo(0, height);
        path.lineTo(0, height * (1 - progress));

        if (wave > 0) {
            for (int x = DP; x < width; x += DP) {
                path.lineTo(x, height * (1 - progress) - wave / 2f * (float) Math.sin(4.0 * Math.PI * x / width));
            }
        }
        path.lineTo(width, height * (1 - progress));
        path.lineTo(width, height);
        path.close();
        return path;
    }

    private static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, Resources.getSystem().getDisplayMetrics());
    }
}