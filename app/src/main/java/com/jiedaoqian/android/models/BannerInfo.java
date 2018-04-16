package com.jiedaoqian.android.models;

/**
 * Created by zenghui on 2017/7/28.
 */

public class BannerInfo {
    private int resource;
    private String resUrl;

    private String prodName;
    private String adWords;
    private String linkUrl;
    private ResBanner.LoanChannelsBean loanChannelsBean;


    public ResBanner.LoanChannelsBean getLoanChannelsBean() {
        return loanChannelsBean;
    }

    public void setLoanChannelsBean(ResBanner.LoanChannelsBean loanChannelsBean) {
        this.loanChannelsBean = loanChannelsBean;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getAdWords() {
        return adWords;
    }

    public void setAdWords(String adWords) {
        this.adWords = adWords;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
