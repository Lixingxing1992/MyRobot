package com.myrobot.org.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.myrobot.org.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lixingxing
 */
public class MyDrawView extends View {

    public static final String TAG = "MyDrawView";

    private Context mContext;
    private Bitmap bitmapBg;
    private Paint mPaint;
    private Path mPath;

    // 是否显示背景
    private boolean isShowBg = true;
    public void setShowBg(boolean showBg) {
        isShowBg = showBg;
        invalidate();
    }

    // 是否开始画画
    private boolean isStartDraw = false;

    // 是否可以画画
    private boolean isCanDraw = true;
    public void setCanDraw(boolean canDraw) {
        isCanDraw = canDraw;
    }

    // 所有轨迹点的集合
    private List<Point> pathList = new ArrayList<>();
    // 检查结果所需要的最少的轨迹点
    private final static int MIN_CHECK_POINT_NUM = 10;
    //
    private final static int MIN_SUCCESS_CHECK_POINT_NUM = 20;

    public MyDrawView(Context context) {
        this(context, null);
    }

    public MyDrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bitmapBg = BitmapFactory.decodeResource(context.getResources(), R.mipmap.main_mouse_bg);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#0b0d21"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(14);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(bitmapBg.getWidth(), bitmapBg.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG,"onDraw");
        if(isShowBg){
            canvas.drawBitmap(bitmapBg, 0, 0, mPaint);
        }
        if(isStartDraw){
            canvas.drawPath(mPath,mPaint);
        }
    }

    float cur_x,cur_y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG,"onTouchEvent");
        if(!isCanDraw){
            return super.onTouchEvent(event);
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.e(TAG,"ACTION_DOWN");
                isStartDraw = true;
                mPath.reset();
                pathList.clear();
                mPath.moveTo(x, y);
                cur_x = x;
                cur_y = y;
                pathList.add(new Point((int)x,(int)y));
            }
            case MotionEvent.ACTION_MOVE: {
                Log.e(TAG,"ACTION_MOVE");
                // 二次曲线方式绘制
                mPath.quadTo(cur_x, cur_y, x, y);
                // 下面这个方法貌似跟上面一样
                cur_x = x;
                cur_y = y;
                pathList.add(new Point((int)x,(int)y));
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:{
                Log.e(TAG,"ACTION_UP");
                if(myDrawResultListener!=null){
                    myDrawResultListener.onDrawFinish();
                }
                break;
            }
            default:
                break;
        }
        return true;
    }

    private MyDrawResultListener myDrawResultListener;
    public void setMyDrawResultListener(MyDrawResultListener myDrawResultListener) {
        this.myDrawResultListener = myDrawResultListener;
    }
    public interface MyDrawResultListener{
        // 笑
        int RESULT_LAUGH = 1;
        // 哭
        int RESULT_CRY = 2;
        // 未知
        int RESULT_DEFAULT = 0;

        void onResult(int code);
        void onDrawFinish();
    }



    // 检查画的结果
    public void checkResult(){
        if(myDrawResultListener == null || pathList.isEmpty()){
            return;
        }
        if(pathList.size() < MIN_CHECK_POINT_NUM){
            myDrawResultListener.onResult(MyDrawResultListener.RESULT_DEFAULT);
        }else{
            int laughNum = 0;
            int cryNum = 0;
            Point firstPoint = pathList.get(0);
            Point lastPoint = pathList.get(pathList.size() - 1);

            for (int i = 1; i < pathList.size() - 1; i++) {
                Point point = pathList.get(i);
                if( (point.y < firstPoint.y)
                        || (point.y < lastPoint.y)
                ){
                    // 哭脸
                    cryNum += 1;
                }else if( (point.y > firstPoint.y)
                        || (point.y > lastPoint.y)
                ){
                    // 笑脸
                    laughNum += 1;
                }else{

                }
            }

            if(laughNum > MIN_SUCCESS_CHECK_POINT_NUM){
                myDrawResultListener.onResult(MyDrawResultListener.RESULT_LAUGH);
            }else if(cryNum > MIN_SUCCESS_CHECK_POINT_NUM){
                myDrawResultListener.onResult(MyDrawResultListener.RESULT_CRY);
            }else{
                myDrawResultListener.onResult(MyDrawResultListener.RESULT_DEFAULT);
            }
        }
    }


    // 重画
    public void reset(){
        isShowBg = true;
        isStartDraw = false;
        isCanDraw = true;
        pathList.clear();
        mPath.reset();
        invalidate();
    }
}
