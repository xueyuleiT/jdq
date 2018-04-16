package com.jiedaoqian.android.models;

import java.io.Serializable;

/**
 * Created by zenghui on 2017/7/28.
 */

public class ProductHotInfo implements Serializable{

    /**
     * id : 1
     * created : 1521130224000
     * updated : 1521213333000
     * deleted : null
     * version : 0
     * name : 借款
     * subTitle : 超快借款
     * iconImageUrl : null
     * bannerImageUrl : null
     * referenceAmount : 2000-50000
     * loanUrl :
     * fundNum : 123
     * star : 4
     * tags : 闪电到账
     * amountRanges : 2000-5000
     * sortIndex : 1
     * isBanner : null
     */

    private int id;
    private long created;
    private long updated;
    private Object deleted;
    private int version;
    private String name;
    private String subTitle;
    private String iconImageUrl;
    private String bannerImageUrl;
    private String referenceAmount;
    private String loanUrl;
    private int fundNum;
    private int star;
    private String tags;
    private String amountRanges;
    private int sortIndex;
    private Object isBanner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public Object getDeleted() {
        return deleted;
    }

    public void setDeleted(Object deleted) {
        this.deleted = deleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getIconImageUrl() {
        return iconImageUrl;
    }

    public void setIconImageUrl(String iconImageUrl) {
        this.iconImageUrl = iconImageUrl;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getReferenceAmount() {
        return referenceAmount;
    }

    public void setReferenceAmount(String referenceAmount) {
        this.referenceAmount = referenceAmount;
    }

    public String getLoanUrl() {
        return loanUrl;
    }

    public void setLoanUrl(String loanUrl) {
        this.loanUrl = loanUrl;
    }

    public int getFundNum() {
        return fundNum;
    }

    public void setFundNum(int fundNum) {
        this.fundNum = fundNum;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAmountRanges() {
        return amountRanges;
    }

    public void setAmountRanges(String amountRanges) {
        this.amountRanges = amountRanges;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Object getIsBanner() {
        return isBanner;
    }

    public void setIsBanner(Object isBanner) {
        this.isBanner = isBanner;
    }
}
