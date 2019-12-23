package com.myrobot.org.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import com.myrobot.org.server.redis.RedisUtils;
import org.json.JSONObject;

/**
 * 电量显示
 *
 * @author Lixingxing
 */
public class MyBatterView extends View {

    private int lastPower = 0;

    private Paint mPaint;//文字的画笔
    private Paint mBatteryPaint;//电池画笔
    private Paint mPowerPaint;//电量画笔
    private float mBatteryStroke = 2;//电池框宽度

    private RectF mBatteryRect;//电池矩形
    private RectF mCapRect;//电池盖矩形
    private RectF mPowerRect;//电量矩形

    public static Typeface typeFace;
    private int specWidthSize, specHeightSize;
    private float textSize;
    private int batteryColor;//电池框颜色
    private int powerColor;//电量颜色
    private int lowPowerColor;//低电颜色

    private boolean isShowText;

    private int power = -1;//当前电量（满电100）

    private float textWidth = 0;//电量文字长度
    private float mCapWidth;

    private boolean isWork = true;
    private boolean isPause = false;

    public void destory(){
        isPause = true;
        isWork = false;
    }
    public void pause(){
        isPause = true;
    }
    public void reStart(){
        isWork = true;
        isPause = false;
    }


    public void setPro(int power) {
        if (this.power == power) {
            return;
        }
        if (power < 0) {
            power = 0;
        } else if (power > 100) {
            power = 100;
        }
        this.power = power;
        invalidate();
    }

    public MyBatterView(Context context) {
        this(context, null);
    }

    public MyBatterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyBatterView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyBatterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        textSize = 22;
        batteryColor = Color.parseColor("#ffffff");
        powerColor = Color.parseColor("#95c11f");
        lowPowerColor = Color.argb(255, 255, 0, 0);
        isShowText = false;
        mCapWidth = 5;
        initPaint();

        new Thread(() -> {
            while (isWork) {
                try {
                    if (!isPause) {
                        String json = RedisUtils.getReJsonValue("state-tree", ".robot.device.battery");
                        JSONObject jsonObject = new JSONObject(json);
                        int chargingStatus = (int) jsonObject.get("chargingStatus");
                        int batterylevel = (int) jsonObject.get("batterylevel");
                        post(() -> {
                            setPro(batterylevel);
                        });
                    }
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void initPaint() {
        /**
         * 设置电池画笔
         */
        mBatteryPaint = new Paint();
        mBatteryPaint.setColor(batteryColor);
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setStyle(Paint.Style.STROKE);
        mBatteryPaint.setStrokeWidth(mBatteryStroke);
        /**
         * 设置电量画笔
         */
        mPowerPaint = new Paint();
        mPowerPaint.setAntiAlias(true);
        mPowerPaint.setStyle(Paint.Style.FILL);

        /**
         * 设置文字画笔
         */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        specWidthSize = MeasureSpec.getSize(widthMeasureSpec);//宽
        specHeightSize = MeasureSpec.getSize(heightMeasureSpec);//高

        setMeasuredDimension(specWidthSize, specHeightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (power <= 20) {
            mPowerPaint.setColor(lowPowerColor);
            mPaint.setColor(lowPowerColor);
        } else {
            mPowerPaint.setColor(powerColor);
            mPaint.setColor(powerColor);
        }
        if (isShowText) {
            String textString = power + "%";
            Rect textRect = new Rect();
            mPaint.getTextBounds(textString, 0, textString.length(), textRect);
            textWidth = textRect.width();
            float textHeight = textRect.height();
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float top = fontMetrics.top;//为基线到字体上边框的距离
            float bottom = fontMetrics.bottom;//为基线到字体下边框的距离
            int baseLineY = (int) (specHeightSize / 2 - top / 2 - bottom / 2);//基线中间点的y轴计算公式
            // canvas.drawText(textString, (int) (width*0.5), baseLineY, mPaint);
            //(-5  距离最右边5)
            canvas.drawText(textString, specWidthSize - textWidth - 10, baseLineY, mPaint);

        } else {
            textWidth = 0;
        }

        /**
         * 设置电池矩形
         */
        float batteryWidth = specWidthSize - mCapWidth - 4;
        mBatteryRect = new RectF(0, 0, batteryWidth, specHeightSize);
        /**
         * 设置电池盖矩形
         */
        mCapRect = new RectF(specWidthSize - mCapWidth, specHeightSize * 0.20f, specWidthSize,
                specHeightSize * 0.8f);

        canvas.drawRect(mBatteryRect, mBatteryPaint);
        canvas.drawRect(mCapRect, mBatteryPaint);// 画电池盖

        if(power > -1){
            /**
             * 设置电量矩形
             */
            float space = 1;
            float powerWidth = batteryWidth - mBatteryStroke * 2 - 2;
            float powerWidthSingle = (powerWidth - space * 4) / 5;
            for (int i = 1; i < 6; i++) {
                if (power >= i * 20) {
                    mPowerRect = new RectF((mBatteryStroke + 1) + (i - 1) * powerWidthSingle + (i - 1) * space,
                            1 + mBatteryStroke,
                            (mBatteryStroke + 1) + i * powerWidthSingle + (i - 1) * space,
                            specHeightSize - mBatteryStroke - 1);
                    canvas.drawRect(mPowerRect, mPowerPaint);// 画电量
                } else {
                    float curPower = (power - (i - 1) * 20) / 20f;
                    mPowerRect = new RectF((mBatteryStroke + 1) + (i - 1) * powerWidthSingle + (i - 1) * space,
                            1 + mBatteryStroke,
                            (mBatteryStroke + 1) + (i - 1) * powerWidthSingle + (i - 1) * space + curPower * powerWidthSingle,
                            specHeightSize - mBatteryStroke - 1);
                    canvas.drawRect(mPowerRect, mPowerPaint);// 画电量
                    break;
                }
            }
        }
    }

}
