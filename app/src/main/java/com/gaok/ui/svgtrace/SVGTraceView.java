package com.gaok.ui.svgtrace;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.gaok.ui.R;
import com.gaok.ui.svgtrace.utils.SvgPathParser;

/**
 * Created by gaok on 2017年1月5日
 */

public class SVGTraceView extends View {
    private Context mContext;
    private float pathWidth;
    private float pathHeight;
    private float mLength;
    private Path mSvgPath;
    private Paint mPaint;
    private float mAnimatorValue;
    public static float translateX;
    public static float translateY;
    private long mDuration = 3000;
    private PathMeasure mSvgPathMeasure;

    public SVGTraceView(Context context) {
        this(context, null);
    }

    public SVGTraceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SVGTraceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        setDrawingCacheEnabled(true);

        if (Build.VERSION.SDK_INT < 21) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mPaint = new Paint();
        //初始化svg路径
        SvgPathParser spp = new SvgPathParser();
        mSvgPath = spp.parsePath(context.getResources().getString(R.string.path));
        //取所有线段的最大值
        mSvgPathMeasure = new PathMeasure(mSvgPath, true);
        while (true) {
            mLength = Math.max(mLength, mSvgPathMeasure.getLength());
            if (!mSvgPathMeasure.nextContour()) {
                break;
            }
        }
        //获取路径图宽高
        pathWidth = spp.getPathWidth();
        pathHeight = spp.getPathHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        //把图形平移至屏幕中心
        translateX = (screenWidth - pathWidth) / 2;
        translateY = (screenHeight - pathHeight) / 2;
        canvas.translate(translateX, translateY);

        drawSVG(canvas);
    }

    private void drawSVG(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        //绘制基本形状
        float distance = mAnimatorValue * mLength;
        mPaint.setPathEffect(new DashPathEffect(new float[]{distance, mLength}, 0));
        canvas.drawPath(mSvgPath, mPaint);
        //绘制跑动线
        mPaint.setColor(mContext.getResources().getColor(R.color.colorAccent));
        mPaint.setStrokeWidth(5);
        float mMarkerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        mPaint.setPathEffect(new DashPathEffect(new float[]{0, distance, mMarkerLength, mLength}, 0));
        canvas.drawPath(mSvgPath, mPaint);
    }

    public void startAnimate() {
        getSvgPathValueAnimator().start();
    }

    @NonNull
    private ValueAnimator getSvgPathValueAnimator() {
        final ValueAnimator svgAnimator = ValueAnimator.ofFloat(0, 1);
        svgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                postInvalidateDelayed(10);
            }
        });
        svgAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                invalidate();
                svgAnimator.removeAllUpdateListeners();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        svgAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        svgAnimator.setDuration(mDuration);
        return svgAnimator;
    }
}