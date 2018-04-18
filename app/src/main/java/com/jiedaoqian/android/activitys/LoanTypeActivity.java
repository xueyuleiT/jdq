package com.jiedaoqian.android.activitys;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.adapters.MainProductAdapter;
import com.jiedaoqian.android.models.MainProductInfo;
import com.jiedaoqian.android.models.ProductHotInfo;
import com.jiedaoqian.android.utils.Common;
import com.jiedaoqian.android.utils.httputils.HttpBaseResponse;
import com.jiedaoqian.android.utils.httputils.HttpUtil;
import com.jiedaoqian.android.utils.httputils.RedirectObserver;
import com.jiedaoqian.android.utils.httputils.RedirectRTransformer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Created by zenghui on 2018/4/16.
 */

public class LoanTypeActivity extends BaseActivity{

    private String title;
    private RecyclerView recycleView;
    private SwipeRefreshLayout swpLayout;
    private MainProductAdapter mainProductAdapter;
    private boolean isLoading = false;
    List<MainProductInfo> mainProductInfos = new ArrayList<>();
    private int pageIndex = 0;
    private int pageSize = 10;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_loan_type);
        title = getIntent().getStringExtra("title");
        setToobar((Toolbar) findViewById(R.id.toolbar),title);

        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(mContext));
        swpLayout = findViewById(R.id.swipeRefreshLayout);

    }

    @Override
    public void initDatas() {

        Common.setSwipeLayout(swpLayout);

        MainProductInfo mainProductInfo = new MainProductInfo();
        mainProductInfo.setViewType(MainProductInfo.VIEW_TYPE_LOADING);
        mainProductInfo.setShowLoading(false);
        mainProductInfos.add(mainProductInfo);

        swpLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(title);
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
                    getLoanchannel(title);
                }
            }
        });

        getLoanchannel(title);

    }

    void refresh(String t){
        pageIndex = 0;
        isLoading = false;

        mainProductInfos.clear();
        MainProductInfo mainProductInfo = new MainProductInfo();
        mainProductInfo.setViewType(MainProductInfo.VIEW_TYPE_LOADING);
        mainProductInfo.setShowLoading(false);
        mainProductInfos.add(mainProductInfo);

        getLoanchannel(t);
    }

    void getLoanchannel(String type){
        swpLayout.setRefreshing(true);
        Observable<Response<HttpBaseResponse<List<ProductHotInfo>>>> observable = HttpUtil.getInstance().getHttpApi().loanchannel(pageIndex,pageSize,"金额不限",type);
        observable.compose(new RedirectRTransformer<Response<HttpBaseResponse<List<ProductHotInfo>>>, HttpBaseResponse<List<ProductHotInfo>>>())
                .subscribe(new RedirectObserver<HttpBaseResponse<List<ProductHotInfo>>>(mContext) {
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
                                    mainProductAdapter = new MainProductAdapter(mContext, mainProductInfos);
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
                                    MainProductAdapter mainProductAdapter = new MainProductAdapter(mContext, mainProductInfos);
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
                                mainProductAdapter = new MainProductAdapter(mContext, mainProductInfos);
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

}
