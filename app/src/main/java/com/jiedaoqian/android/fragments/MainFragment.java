package com.jiedaoqian.android.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.activitys.BaseActivity;
import com.jiedaoqian.android.adapters.MainProductAdapter;
import com.jiedaoqian.android.models.BannerInfo;
import com.jiedaoqian.android.models.MainProductInfo;
import com.jiedaoqian.android.models.ProductHotInfo;
import com.jiedaoqian.android.models.ResBanner;
import com.jiedaoqian.android.utils.Common;
import com.jiedaoqian.android.utils.DialogUtil;
import com.jiedaoqian.android.utils.GlideImageLoader;
import com.jiedaoqian.android.utils.SettingUtil;
import com.jiedaoqian.android.utils.httputils.HttpBaseResponse;
import com.jiedaoqian.android.utils.httputils.HttpUtil;
import com.jiedaoqian.android.utils.httputils.RedirectObserver;
import com.jiedaoqian.android.utils.httputils.RedirectRTransformer;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;
import retrofit2.http.HTTP;

/**
 * Created by zenghui on 2018/3/19.
 */

public class MainFragment extends BaseFragment {
    private View rootView;
    private boolean init = false;
    private RecyclerView recycleView;
    private SwipeRefreshLayout swpLayout;
    MainProductAdapter mainProductAdapter;
    private boolean isLoading = false;
    List<MainProductInfo> mainProductInfos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, null);
            HttpUtil.ut = SettingUtil.get(getActivity(),"token","");
            Common.scanHistory = SettingUtil.getObjFromSp(getActivity(),Common.scanSpName,Common.scanSpKey+SettingUtil.get(getActivity(),"phone",""));
            if (Common.scanHistory == null){
                Common.scanHistory = new ArrayList<>(30);
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!init) {
            init = true;

            recycleView = rootView.findViewById(R.id.recycleView);
            swpLayout = rootView.findViewById(R.id.swipeRefreshLayout);
            Common.setSwipeLayout(swpLayout);


            swpLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pageIndex = 0;
                    isLoading = false;

                    mainProductInfos.clear();
                    tags();
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
            tags();
        }

    }
    private int pageIndex = 0;
    private int pageSize = 10;

    void tags(){
        Observable<Response<HttpBaseResponse<ResBanner>>> observable = HttpUtil.getInstance().getHttpApi().banners();
        observable.compose(new RedirectRTransformer<Response<HttpBaseResponse<ResBanner>>, HttpBaseResponse<ResBanner>>())
                .subscribe(new RedirectObserver<HttpBaseResponse<ResBanner>>(getActivity()){

                    @Override
                    public void onNext(HttpBaseResponse<ResBanner> value) {
                        if (HttpUtil.SUCCESS.equals(value.getCode())){
                            List<ResBanner.LoanChannelsBean> dataBeans = value.getData().getLoanChannels();

                            MainProductInfo mainProductInfo = new MainProductInfo();
                            mainProductInfo.setViewType(MainProductInfo.VIEW_TYPE_HEADER);
                            List<BannerInfo> bannerInfos = new ArrayList<>(5);
                            int size = dataBeans.size();
                            for (int i = 0 ; i < size ; i ++){
                                ResBanner.LoanChannelsBean dataBean = dataBeans.get(i);
                                BannerInfo bannerInfo = new BannerInfo();
                                bannerInfo.setResUrl(HttpUtil.BASE_URL+dataBean.getBannerImageUrl());
                                bannerInfo.setLinkUrl(HttpUtil.BASE_URL+dataBean.getLoanUrl());
                                bannerInfo.setLoanChannelsBean(dataBean);
                                bannerInfos.add(bannerInfo);
                            }
                            mainProductInfo.setAds(value.getData().getFunders());
                            mainProductInfo.setBannerInfoList(bannerInfos);
                            mainProductInfos.add(mainProductInfo);

                            mainProductInfo = new MainProductInfo();
                            mainProductInfo.setViewType(MainProductInfo.VIEW_TYPE_ARROW);
                            mainProductInfo.setTitle("热门产品");
                            mainProductInfos.add(mainProductInfo);

                            mainProductInfo = new MainProductInfo();
                            mainProductInfo.setViewType(MainProductInfo.VIEW_TYPE_LOADING);
                            mainProductInfo.setShowLoading(false);
                            mainProductInfos.add(mainProductInfo);

                        }
                        getLoanchannel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getLoanchannel();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });

    }

    void getLoanchannel(){
        swpLayout.setRefreshing(true);
        Observable<Response<HttpBaseResponse<List<ProductHotInfo>>>> observable = HttpUtil.getInstance().getHttpApi().loanchannel(pageIndex,pageSize);
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
                                    mainProductInfos.get(mainProductInfos.size() - 1).setShowLoading(false);
                                    mainProductAdapter = new MainProductAdapter(getActivity(), mainProductInfos);
                                    recycleView.setAdapter(mainProductAdapter);
                                    recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                } else {
//                                    if (mainProductInfos.size() > 0) {
//                                        mainProductInfos.remove(mainProductInfos.size() - 1);
//                                    }
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
                                    recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                                recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
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



}
