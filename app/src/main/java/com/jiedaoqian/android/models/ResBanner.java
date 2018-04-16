package com.jiedaoqian.android.models;

import java.util.List;

/**
 * Created by zenghui on 2018/3/31.
 */

public class ResBanner {


    private List<LoanChannelsBean> loanChannels;
    private List<String> funders;

    public List<LoanChannelsBean> getLoanChannels() {
        return loanChannels;
    }

    public void setLoanChannels(List<LoanChannelsBean> loanChannels) {
        this.loanChannels = loanChannels;
    }

    public List<String> getFunders() {
        return funders;
    }

    public void setFunders(List<String> funders) {
        this.funders = funders;
    }

    public static class LoanChannelsBean {
        /**
         * id : 1
         * created : 1521130224000
         * updated : 1521213333000
         * deleted : null
         * version : 0
         * name : 借款
         * subTitle : 超快借款
         * iconImageUrl : /images/1.jpg
         * bannerImageUrl : /images/1.jpg
         * referenceAmount : 2000-50000
         * loanUrl : /images/1.jpg
         * fundNum : 123
         * star : 4
         * tags : 闪电到账
         * amountRanges : 2000-5000
         * sortIndex : 1
         * isBanner : true
         * isHot : true
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
        private boolean isBanner;
        private boolean isHot;

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

        public boolean isIsBanner() {
            return isBanner;
        }

        public void setIsBanner(boolean isBanner) {
            this.isBanner = isBanner;
        }

        public boolean isIsHot() {
            return isHot;
        }

        public void setIsHot(boolean isHot) {
            this.isHot = isHot;
        }
    }
}
