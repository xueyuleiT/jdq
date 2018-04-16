package com.jiedaoqian.android.models;

import java.util.List;

/**
 * Created by zenghui on 2017/7/28.
 */

public class MainProductInfo {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_SUGGEST = 1;
    public static final int VIEW_TYPE_HOT = 2;
    public static final int VIEW_TYPE_ARROW = 3;
    public static final int VIEW_TYPE_LOADING = 4;
    int viewType;
    private boolean showLoading = false;
    List<String> ads;
    List<BannerInfo> bannerInfoList;
    ProductHotInfo hotInfo;
    String title;


    public List<String> getAds() {
        return ads;
    }

    public void setAds(List<String> ads) {
        this.ads = ads;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public List<BannerInfo> getBannerInfoList() {
        return bannerInfoList;
    }

    public void setBannerInfoList(List<BannerInfo> bannerInfoList) {
        this.bannerInfoList = bannerInfoList;
    }

    public ProductHotInfo getHotInfo() {
        return hotInfo;
    }

    public void setHotInfo(ProductHotInfo hotInfo) {
        this.hotInfo = hotInfo;
    }
}
