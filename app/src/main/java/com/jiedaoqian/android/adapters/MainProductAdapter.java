package com.jiedaoqian.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiedaoqian.android.R;
import com.jiedaoqian.android.activitys.WebviewActivity;
import com.jiedaoqian.android.login.LoginActivity;
import com.jiedaoqian.android.models.BannerInfo;
import com.jiedaoqian.android.models.MainProductInfo;
import com.jiedaoqian.android.models.ProductHotInfo;
import com.jiedaoqian.android.utils.Common;
import com.jiedaoqian.android.utils.GlideImageLoader;
import com.jiedaoqian.android.utils.SettingUtil;
import com.jiedaoqian.android.utils.httputils.HttpUtil;
import com.jiedaoqian.android.views.ScrollAdView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jiedaoqian.android.models.MainProductInfo.VIEW_TYPE_HOT;

/**
 * Created by zenghui on 2017/7/28.
 */

public class MainProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<MainProductInfo> list;
    Context context;
    public MainProductAdapter(Context context, List<MainProductInfo> list){
        this.list = list;
        this.context = context;
    }
    @Override public int getItemViewType(int position) {
        MainProductInfo mainProductInfo = list.get(position);

        return mainProductInfo.getViewType();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MainProductInfo.VIEW_TYPE_HEADER){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
        }else if (viewType == MainProductInfo.VIEW_TYPE_SUGGEST){
            return new SuggestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
        }else if (viewType == VIEW_TYPE_HOT){
            return new HotViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
        }else if (viewType == MainProductInfo.VIEW_TYPE_ARROW){
            return new ArrowHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arrow, parent, false));
        }else {
            return new LoadingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MainProductInfo mainProductInfo = list.get(position);
        if (mainProductInfo.getViewType() == MainProductInfo.VIEW_TYPE_HEADER){
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.banner.setImageLoader(new GlideImageLoader());
            headerViewHolder.adView.initData(mainProductInfo.getAds(), (Activity) context);
            List<String> bannerPics = new ArrayList<>();
            List<BannerInfo> bannerInfos = mainProductInfo.getBannerInfoList();
            for (BannerInfo bannerInfo : bannerInfos){
                bannerPics.add(bannerInfo.getResUrl());
            }
            //设置图片集合
            headerViewHolder.banner.setImages(bannerPics);
            headerViewHolder.banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int mPosition) {

                    if (TextUtils.isEmpty(SettingUtil.get(context,"token",""))) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).overridePendingTransition(com.jiedaoqian.android.R.anim.slide_bottom_in, com.jiedaoqian.android.R.anim.slide_bottom_out);
                        return;
                    }

                    BannerInfo bannerInfo = list.get(position).getBannerInfoList().get(mPosition);
                    if (!TextUtils.isEmpty(bannerInfo.getLinkUrl())){
                        int size = Common.scanHistory.size();
                        boolean find = false;
                        for (int i = 0; i < size; i ++){
                            if (bannerInfo.getLinkUrl().equals(Common.scanHistory.get(i).getLoanUrl())){
                                find = true;
                                break;
                            }
                        }
                        if (!find){
                            ProductHotInfo productHotInfo = new ProductHotInfo();
                            productHotInfo.setAmountRanges(bannerInfo.getLoanChannelsBean().getAmountRanges());
                            productHotInfo.setIconImageUrl(bannerInfo.getLoanChannelsBean().getIconImageUrl());
                            productHotInfo.setFundNum(bannerInfo.getLoanChannelsBean().getFundNum());
                            productHotInfo.setLoanUrl(bannerInfo.getLoanChannelsBean().getLoanUrl());
                            productHotInfo.setName(bannerInfo.getLoanChannelsBean().getName());
                            productHotInfo.setSubTitle(bannerInfo.getLoanChannelsBean().getSubTitle());
                            productHotInfo.setReferenceAmount(bannerInfo.getLoanChannelsBean().getReferenceAmount());
                            productHotInfo.setTags(bannerInfo.getLoanChannelsBean().getTags());
                            Common.scanHistory.add(0,productHotInfo);
                            if (Common.scanHistory.size() > 30){
                                Common.scanHistory.remove(Common.scanHistory.size() - 1);
                            }
                            SettingUtil.saveObj2SP(context,Common.scanHistory,Common.scanSpName,Common.scanSpKey+
                                    SettingUtil.get(context,"phone",""));
                        }

                        Intent intent = new Intent(context, WebviewActivity.class);
                        intent.putExtra(Common.TITLE,bannerInfo.getLoanChannelsBean().getName());
                        intent.putExtra(Common.LINK,bannerInfo.getLoanChannelsBean().getLoanUrl());
                        context.startActivity(intent);

                    }
                }
            });

            //banner设置方法全部调用完毕时最后调用
            headerViewHolder.banner.start();
        }else if (mainProductInfo.getViewType() == MainProductInfo.VIEW_TYPE_SUGGEST){

        }else if (mainProductInfo.getViewType() == MainProductInfo.VIEW_TYPE_HOT){
            final HotViewHolder hotViewHolder = (HotViewHolder) holder;
            final ProductHotInfo hotInfo = mainProductInfo.getHotInfo();
            hotViewHolder.tvSubTitle.setText(TextUtils.isEmpty(hotInfo.getName())?"":hotInfo.getName());
            hotViewHolder.tvTags.setText(TextUtils.isEmpty(hotInfo.getTags())?"":hotInfo.getTags());
            hotViewHolder.tvFundNum.setText(hotInfo.getFundNum()+"人已放款");
            hotViewHolder.tvReferenceAmount.setText("参考额度:"+(TextUtils.isEmpty(hotInfo.getReferenceAmount())?"":hotInfo.getReferenceAmount()));
            hotViewHolder.starLayout.removeAllViews();
            for (int i =0 ; i < hotInfo.getStar() ; i ++){
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.mipmap.ic_star);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.dp_18)
                ,context.getResources().getDimensionPixelSize(R.dimen.dp_18));
                layoutParams.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_5);
                imageView.setLayoutParams(layoutParams);
                hotViewHolder.starLayout.addView(imageView);
            }

            hotViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(SettingUtil.get(context,"token",""))) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).overridePendingTransition(com.jiedaoqian.android.R.anim.slide_bottom_in, com.jiedaoqian.android.R.anim.slide_bottom_out);
                        return;
                    }

                    if (!TextUtils.isEmpty(hotInfo.getLoanUrl())){
                        int size = Common.scanHistory.size();
                        boolean find = false;
                        for (int i = 0; i < size; i ++){
                            if (hotInfo.getLoanUrl().equals(Common.scanHistory.get(i).getLoanUrl())){
                                find = true;
                                break;
                            }
                        }
                        if (!find){
                             Common.scanHistory.add(0,hotInfo);
                            if (Common.scanHistory.size() > 30){
                                Common.scanHistory.remove(Common.scanHistory.size() - 1);
                            }
                            SettingUtil.saveObj2SP(context,Common.scanHistory,Common.scanSpName,Common.scanSpKey+
                                    SettingUtil.get(context,"phone",""));                        }

                        Intent intent = new Intent(context, WebviewActivity.class);
                        intent.putExtra(Common.TITLE,hotInfo.getName());
                        intent.putExtra(Common.LINK,hotInfo.getLoanUrl());
                        context.startActivity(intent);


                    }

                }
            });
            Glide.with(context).load(HttpUtil.BASE_URL+hotInfo.getIconImageUrl()).into(hotViewHolder.imgUrl);

        }else if (mainProductInfo.getViewType() == MainProductInfo.VIEW_TYPE_ARROW){
            ArrowHolder arrowHolder = (ArrowHolder) holder;
            arrowHolder.title.setText(mainProductInfo.getTitle());
        }else {
            LoadingHolder loadingHolder = (LoadingHolder) holder;
            if (mainProductInfo.isShowLoading()){
                loadingHolder.rootView.setVisibility(View.VISIBLE);
            }else {
                loadingHolder.rootView.setVisibility(View.GONE);
            }
        }

    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{
        Banner banner;
        ScrollAdView adView;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
            adView = itemView.findViewById(R.id.adView);
        }
    }

    class SuggestViewHolder extends RecyclerView.ViewHolder{
        TextView title,describe,money;
        ImageView img;
        public SuggestViewHolder(View itemView) {
            super(itemView);

        }
    }

    class HotViewHolder extends RecyclerView.ViewHolder{
        TextView tvTags,tvReferenceAmount,tvFundNum,tvSubTitle;
        ImageView imgUrl;
        LinearLayout starLayout;
        View rootView;
        public HotViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            tvTags = itemView.findViewById(R.id.tvTags);
            tvReferenceAmount = itemView.findViewById(R.id.tvReferenceAmount);
            tvFundNum = itemView.findViewById(R.id.tvFundNum);
            tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
            imgUrl = itemView.findViewById(R.id.imgUrl);
            starLayout = itemView.findViewById(R.id.starLayout);


        }
    }

    class ArrowHolder extends RecyclerView.ViewHolder{

        TextView title;
        public ArrowHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    class LoadingHolder extends RecyclerView.ViewHolder{

        View rootView;
        public LoadingHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
