package com.jiedaoqian.android.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.utils.TextViewUtil.TextUtil;

import java.util.List;


/**
 * Created by zenghui on 2018/3/21.
 */

public class ScrollAdView extends FrameLayout{

    private int height;
    private int flag=1;
    private List<String> stringList;
    private LinearLayout linearLayoutContent;
    private ObjectAnimator animator;

    private View.OnClickListener listener=null;



    public ScrollAdView(Context context) {
        super(context);
    }

    public ScrollAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void initData(List<String> adList,Activity activity)
    {
        height=this.getMeasuredHeight();
        if (height == 0){
            height = getResources().getDimensionPixelSize(R.dimen.dp_25);
        }

        if (animator != null && animator.isRunning()){
            animator.cancel();
        }

        removeAllViews();
        linearLayoutContent=new LinearLayout(activity);
        linearLayoutContent.setOrientation(LinearLayout.VERTICAL);
        linearLayoutContent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,height*2));  //!!! 必须设置height ，否则为AT_MOST

        stringList=adList;

        for(int i=0;i<2;i++)
        {
            TextView tv=new TextView(activity);
            tv.setSingleLine(true);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.sp_12));
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,height));
            tv.setText(stringList.get(i));
            TextUtil.changeColor(tv,getResources().getDimensionPixelSize(R.dimen.sp_12),stringList.get(i).indexOf("申请的")+3,stringList.get(i).indexOf("元的贷款"), Color.parseColor("#ff0000"));
            tv.setTag(stringList.get(i)+i);
            tv.setGravity(Gravity.CENTER_VERTICAL);

            tv.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v) {

                    if(listener!=null)
                    {
                        listener.onClick(v);
                    }
                }
            });

            linearLayoutContent.addView(tv);
        }

        this.addView(linearLayoutContent);

        initAnimation();

    }

    public void initAnimation()
    {

        //方法1====start====
//        Animation animation =new TranslateAnimation(0,0,0,-height);
//        animation.setDuration(2000);
//        animation.setRepeatMode(ValueAnimator.RESTART);
//        animation.setRepeatCount(ObjectAnimator.INFINITE);
//        animation.setInterpolator(new DecelerateInterpolator());
//
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation)
//            {
//                TextView tv=(TextView)linearLayoutContent.getChildAt(0);
//                linearLayoutContent.removeView(tv);
//                linearLayoutContent.addView(tv);
//
//                if(flag+1>=stringList.size())
//                {
//                    flag=0;
//                }else
//                {
//                    flag=flag+1;
//                }
//                tv.setText(stringList.get(flag));
//                tv.setTag(stringList.get(flag));
//                tv.setGravity(Gravity.CENTER_VERTICAL);
//            }
//        });
//        linearLayoutContent.startAnimation(animation);
        //方法1====end====

        //方法2===start====
        animator = ObjectAnimator.ofFloat(linearLayoutContent,"translationY",0,-height);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
//        animator.setRepeatMode(ValueAnimator.RESTART);
//        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();

//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            boolean isPause = false;
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float y = (float) animation.getAnimatedValue();
//                Log.d("y ===",""+y+ "  height = "+height);
//                if (Math.abs(height+y) < 1 && !isPause){
//                    isPause = true;
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }else if (Math.abs(height+y) >= 1){
//                    isPause = false;
//                }
//            }
//        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                TextView tv=(TextView)linearLayoutContent.getChildAt(0);
                linearLayoutContent.removeView(tv);
                linearLayoutContent.addView(tv);

                if(flag+1>=stringList.size())
                {
                    flag=0;
                }else
                {
                    flag=flag+1;
                }
                tv.setText(stringList.get(flag));
                TextUtil.changeColor(tv,getResources().getDimensionPixelSize(R.dimen.sp_12),tv.getText().toString().indexOf("申请的")+3,tv.getText().toString().indexOf("元的贷款"), Color.parseColor("#ff0000"));
                tv.setTag(stringList.get(flag)+flag);
                tv.setGravity(Gravity.CENTER_VERTICAL);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator.setStartDelay(2000);
                animator.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {




            }
        });

        //方法2===end====

    }

    public void cancelAnimation()
    {
        animator.cancel();
    }

}
