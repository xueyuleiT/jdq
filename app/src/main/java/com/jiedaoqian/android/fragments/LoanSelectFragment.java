package com.jiedaoqian.android.fragments;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.adapters.MainProductAdapter;
import com.jiedaoqian.android.models.BannerInfo;
import com.jiedaoqian.android.models.MainProductInfo;
import com.jiedaoqian.android.models.ProductHotInfo;
import com.jiedaoqian.android.utils.AnimateUtil;
import com.jiedaoqian.android.utils.Common;
import com.jiedaoqian.android.utils.httputils.HttpBaseResponse;
import com.jiedaoqian.android.utils.httputils.HttpUtil;
import com.jiedaoqian.android.utils.httputils.RedirectObserver;
import com.jiedaoqian.android.utils.httputils.RedirectRTransformer;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Created by zenghui on 2018/3/21.
 */

public class LoanSelectFragment extends BaseFragment implements View.OnClickListener{

    private View rootView;
    private boolean init = false;
    private RecyclerView recycleView;
    private SwipeRefreshLayout swpLayout;
    private MainProductAdapter mainProductAdapter;
    private boolean isLoading = false;
    private RelativeLayout rlLeft,rlRight;
    private View bg;
    private LinearLayout loanType,loanAmount;
    private TextView tvSelectLeft,tvSelectRight;
    private ImageView imgLeft,imgRight;
    List<MainProductInfo> mainProductInfos = new ArrayList<>();

    private String range = "金额不限",type = "闪电到账";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_loanselect, null);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!init) {
            init = true;

            tvSelectLeft = rootView.findViewById(R.id.tvSelectLeft);
            tvSelectRight = rootView.findViewById(R.id.tvSelectRight);
            recycleView = rootView.findViewById(R.id.recycleView);
            recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

            swpLayout = rootView.findViewById(R.id.swipeRefreshLayout);
            rlLeft = rootView.findViewById(R.id.rlLeft);
            rlRight = rootView.findViewById(R.id.rlRight);
            loanType = rootView.findViewById(R.id.loanType);
            loanAmount = rootView.findViewById(R.id.loanAmount);
            imgLeft = rootView.findViewById(R.id.imgLeft);
            imgRight = rootView.findViewById(R.id.imgRight);

            for (int i = 0 ; i < loanAmount.getChildCount(); i ++){
                LinearLayout child = (LinearLayout) loanAmount.getChildAt(i);
                for (int j=0; j < child.getChildCount(); j ++){
                    child.getChildAt(j).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (loanType.getAnimation() != null || loanAmount.getAnimation() != null){
                                return;
                            }
                            range = ((TextView)v).getText().toString();
                            tvSelectRight.setTextColor(Color.parseColor("#363636"));
                            tvSelectRight.setText(range);
                            changeLoanAmount();
                            bg.setVisibility(View.GONE);
                            if (loanType.getVisibility() != View.GONE) {
                                AnimateUtil.disappearAnimation(loanType);
                                rotateImg(imgLeft,180,360);

                            }
                            if (loanAmount.getVisibility() != View.GONE) {
                                AnimateUtil.disappearAnimation(loanAmount);
                                rotateImg(imgRight,180,360);

                            }
                            refresh(range,type);
                        }
                    });
                }
            }

            for (int i = 0; i < loanType.getChildCount(); i ++){
                TextView textView = (TextView) loanType.getChildAt(i);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (loanType.getAnimation() != null || loanAmount.getAnimation() != null){
                            return;
                        }
                        type = ((TextView)v).getText().toString();
                        tvSelectLeft.setTextColor(Color.parseColor("#363636"));
                        tvSelectLeft.setText(type);
                        changeType();
                        bg.setVisibility(View.GONE);
                        if (loanType.getVisibility() != View.GONE) {
                            AnimateUtil.disappearAnimation(loanType);
                            rotateImg(imgLeft,180,360);

                        }
                        if (loanAmount.getVisibility() != View.GONE) {
                            AnimateUtil.disappearAnimation(loanAmount);
                            rotateImg(imgRight,180,360);

                        }
                        refresh(range,type);
                    }
                });
            }

            bg = rootView.findViewById(R.id.bg);
            bg.setOnClickListener(null);
            bg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (loanType.getAnimation() != null || loanAmount.getAnimation() != null){
                        return false;
                    }
                    tvSelectRight.setTextColor(Color.parseColor("#363636"));
                    tvSelectLeft.setTextColor(Color.parseColor("#363636"));

                    bg.setVisibility(View.GONE);
                    if (loanType.getVisibility() != View.GONE) {
                        AnimateUtil.disappearAnimation(loanType);
                        rotateImg(imgLeft,180,360);

                    }
                    if (loanAmount.getVisibility() != View.GONE) {
                        AnimateUtil.disappearAnimation(loanAmount);
                        rotateImg(imgRight,180,360);

                    }
                    return true;
                }
            });
            Common.setSwipeLayout(swpLayout);

            MainProductInfo mainProductInfo = new MainProductInfo();
            mainProductInfo.setViewType(MainProductInfo.VIEW_TYPE_LOADING);
            mainProductInfo.setShowLoading(false);
            mainProductInfos.add(mainProductInfo);


            swpLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                   refresh(range,type);
                }
            });

            recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                LinearLayoutManager linearLayoutManager;

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    linearLayoutManager = (LinearLayoutManager) recycleView.getLayoutManager();
                    if (linearLayoutManager == null){
                        return;
                    }
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (!isLoading && lastVisibleItemPosition > 0 && totalItemCount <= lastVisibleItemPosition + 1 && lastVisibleItemPosition - firstVisibleItemPosition < totalItemCount - 1) {
                        //此时是刷新状态
                        isLoading = true;
                        mainProductInfos.get(mainProductInfos.size() - 1).setShowLoading(true);
                        recyclerView.post(new Runnable() {
                            public void run() {
                                mainProductAdapter.notifyDataSetChanged();
                            }
                        });
                        getLoanchannel();
                    }
                }
            });

            rlLeft.setOnClickListener(this);
            rlRight.setOnClickListener(this);
            getLoanchannel();
        }
    }


    private int pageIndex = 0;
    private int pageSize = 10;
    void getLoanchannel(){
        swpLayout.setRefreshing(true);
        Observable<Response<HttpBaseResponse<List<ProductHotInfo>>>> observable = HttpUtil.getInstance().getHttpApi().loanchannel(pageIndex,pageSize,range,type);
        observable.compose(new RedirectRTransformer<Response<HttpBaseResponse<List<ProductHotInfo>>>, HttpBaseResponse<List<ProductHotInfo>>>())
                .subscribe(new RedirectObserver<HttpBaseResponse<List<ProductHotInfo>>>(getActivity()) {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpBaseResponse<List<ProductHotInfo>> value) {
                        swpLayout.setRefreshing(false);
                        isLoading = false;
                        if (value.getCode().equals(HttpUtil.SUCCESS)) {

                            MainProductInfo mainProductInfo;

                            List<ProductHotInfo> hotInfos = value.getData();
                            if (hotInfos != null){
                                int length = hotInfos.size();
                                for (int i = 0 ; i < length ; i ++){
                                    mainProductInfo = new MainProductInfo();
                                    ProductHotInfo hotInfo = hotInfos.get(i);
                                    mainProductInfo.setHotInfo(hotInfo);
                                    mainProductInfo.setViewType(MainProductInfo.VIEW_TYPE_HOT);
                                    mainProductInfos.add(mainProductInfos.size() -1,mainProductInfo);
                                }

                                if (length < pageSize){
                                    isLoading = true;
                                    mainProductInfos.remove(mainProductInfos.size() - 1);
                                }else {
                                    pageIndex ++;
                                }

                                if (mainProductAdapter == null) {
//                                    if (mainProductInfos.size() > 0) {
//                                        mainProductInfos.get(mainProductInfos.size() - 1).setShowLoading(false);
//                                    }
                                    mainProductAdapter = new MainProductAdapter(getActivity(), mainProductInfos);
                                    recycleView.setAdapter(mainProductAdapter);
                                } else {
                                    if (mainProductInfos.size() > 0) {
                                        mainProductInfos.get(mainProductInfos.size() - 1).setShowLoading(false);
                                    }
                                    mainProductAdapter.notifyDataSetChanged();
                                }

                            }else {
                                isLoading = true;
                                if (mainProductInfos.size() > 0) {
                                    mainProductInfos.remove(mainProductInfos.size() - 1);
                                }
                                if ( mainProductAdapter == null) {
                                    MainProductAdapter mainProductAdapter = new MainProductAdapter(getActivity(), mainProductInfos);
                                    recycleView.setAdapter(mainProductAdapter);
                                } else {
                                    mainProductAdapter.notifyDataSetChanged();
                                }
                            }


                        }
                        if (mainProductInfos == null || mainProductInfos.size() == 0){
                            recycleView.setVisibility(View.GONE);
                        }else {
                            recycleView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        swpLayout.setRefreshing(false);
                        isLoading = false;
                        if (mainProductInfos.size() > 0){
                            if (mainProductAdapter == null) {
                                mainProductInfos.get(mainProductInfos.size() - 1).setShowLoading(false);
                                mainProductAdapter = new MainProductAdapter(getActivity(), mainProductInfos);
                                recycleView.setAdapter(mainProductAdapter);
                            } else {
                                mainProductInfos.get(mainProductInfos.size() - 1).setShowLoading(false);
                                mainProductAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        swpLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlLeft:
                if (loanType.getAnimation() != null || loanAmount.getAnimation() != null){
                    return;
                }

                tvSelectLeft.setTextColor(Color.parseColor("#4A61B8"));
                tvSelectRight.setTextColor(Color.parseColor("#363636"));
                if (loanType.getVisibility() == View.GONE) {
                    bg.setVisibility(View.VISIBLE);
                    if (loanAmount.getVisibility() != View.GONE) {
                        AnimateUtil.disappearAnimation(loanAmount);
                        rotateImg(imgRight,180,360);
                    }
                    AnimateUtil.appearAnimation(loanType);
                    rotateImg(imgLeft,0,180);


                }else if (loanType.getVisibility() != View.GONE){
                    bg.setVisibility(View.GONE);
                    tvSelectLeft.setTextColor(Color.parseColor("#363636"));
                    AnimateUtil.disappearAnimation(loanType);
                    rotateImg(imgLeft,180,360);


                }
                break;
            case R.id.rlRight:
                if (loanAmount.getAnimation() != null || loanType.getAnimation() != null){
                    return;
                }
                tvSelectLeft.setTextColor(Color.parseColor("#363636"));
                tvSelectRight.setTextColor(Color.parseColor("#4A61B8"));
                if (loanAmount.getVisibility() == View.GONE) {
                    bg.setVisibility(View.VISIBLE);
                    if (loanType.getVisibility() != View.GONE) {
                        AnimateUtil.disappearAnimation(loanType);
                        rotateImg(imgLeft,180,360);

                    }
                    AnimateUtil.appearAnimation(loanAmount);
                    rotateImg(imgRight,0,180);
                }else if (loanAmount.getVisibility() != View.GONE){
                    bg.setVisibility(View.GONE);
                    tvSelectRight.setTextColor(Color.parseColor("#363636"));
                    AnimateUtil.disappearAnimation(loanAmount);
                    rotateImg(imgRight,180,360);


                }
                break;
        }
    }

    void refresh(String r,String t){
        pageIndex = 0;
        isLoading = false;
        range = r;
        type = t;

        mainProductInfos.clear();
        MainProductInfo mainProductInfo = new MainProductInfo();
        mainProductInfo.setViewType(MainProductInfo.VIEW_TYPE_LOADING);
        mainProductInfo.setShowLoading(false);
        mainProductInfos.add(mainProductInfo);

        getLoanchannel();
    }

    void changeLoanAmount(){
        for (int i = 0 ; i < loanAmount.getChildCount(); i ++){
            LinearLayout child = (LinearLayout) loanAmount.getChildAt(i);
            for (int j=0; j < child.getChildCount(); j ++){
               TextView textView = (TextView) child.getChildAt(j);
               if (textView.getText().toString().equals(range)){
                   textView.setTextColor(Color.parseColor("#4a61b8"));
                   textView.setBackgroundResource(R.drawable.shape_r2_b1_c4a61b8);
               }else {
                   textView.setTextColor(Color.parseColor("#abcabc"));
                   textView.setBackgroundResource(R.drawable.shape_r2_b1_cbcbcbc);
               }
            }
        }
    }

    void changeType(){
        for (int i=0; i < loanType.getChildCount(); i ++) {
            TextView textView = (TextView) loanType.getChildAt(i);
            if (textView.getText().toString().equals(type)){
                textView.setTextColor(Color.parseColor("#4a61b8"));
                textView.setBackgroundResource(R.drawable.shape_r2_b1_c4a61b8);
            }else {
                textView.setTextColor(Color.parseColor("#abcabc"));
                textView.setBackgroundResource(R.drawable.shape_r2_b1_cbcbcbc);
            }
        }

    }

    void rotateImg(ImageView rotateImage,float lastAngle,float progress){
        Animation rotateAnimation  = new RotateAnimation(lastAngle, progress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(500);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateImage.startAnimation(rotateAnimation);
    }
}
