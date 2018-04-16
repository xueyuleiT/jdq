package com.jiedaoqian.android.models;

import java.util.List;

/**
 * Created by zenghui on 2018/4/14.
 */

public class ResStatistics {
    private List<FundsBean> funds;
    private List<StatisticsesBean> statisticses;

    public List<FundsBean> getFunds() {
        return funds;
    }

    public void setFunds(List<FundsBean> funds) {
        this.funds = funds;
    }

    public List<StatisticsesBean> getStatisticses() {
        return statisticses;
    }

    public void setStatisticses(List<StatisticsesBean> statisticses) {
        this.statisticses = statisticses;
    }

    public static class FundsBean {
        /**
         * funder : 成都市的万先生今日申请的{amount}的贷款已成功放款
         * amount : 1000元
         */

        private String funder;
        private String amount;

        public String getFunder() {
            return funder;
        }

        public void setFunder(String funder) {
            this.funder = funder;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }

    public static class StatisticsesBean {
        /**
         * title : 评价借款时间
         * subTitle : 25分钟
         */

        private String title;
        private String subTitle;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }
    }
}
