package com.jiedaoqian.android.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.jiedaoqian.android.R;


/**
 * Created by zenghui on 2017/8/14.
 */

public class LoadingView extends View{

    Paint mPaint;
    ValueAnimator valueAnimator;
    int cycleWidth;

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
        cycleWidth = getResources().getDimensionPixelSize(R.dimen.dp_2);
        mPaint.setStrokeWidth(cycleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    void startAnim1(){
        valueAnimator = ValueAnimator.ofInt(10,180);
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                postInvalidate();
                if (progress == 180){
                    startAnim2();
                }
            }
        });
        valueAnimator.start();
    }

    void startAnim2(){
        valueAnimator = ValueAnimator.ofInt(180,350);
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                postInvalidate();
                if (progress == 350){
                    startAnim1();
                }
            }
        });

        valueAnimator.start();
    }

    int progress = 20;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() > 0){
                canvas.save();
            if (progress < 180) {
                canvas.translate(getWidth() * ((180 - progress) % 180) / (2 * 180), getHeight()/4);
                Path path = new Path();
                RectF rect = new RectF(cycleWidth, cycleWidth, getWidth() - cycleWidth, getHeight() - cycleWidth);
                path.arcTo(rect, 180, progress);
                canvas.drawPath(path, mPaint);
            }else {
                canvas.translate(getWidth() * ((180 - progress) % 180) / (2 * 180), getHeight()/4);
                Path path = new Path();
                RectF rect = new RectF(cycleWidth, cycleWidth, getWidth() - cycleWidth, getHeight() - cycleWidth);
                path.arcTo(rect, progress, 360 - progress);
                canvas.drawPath(path, mPaint);
            }
                canvas.restore();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null){
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (valueAnimator == null){
           startAnim1();
        }
    }
}
