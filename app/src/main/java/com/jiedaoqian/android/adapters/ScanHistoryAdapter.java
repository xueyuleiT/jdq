package com.jiedaoqian.android.adapters;

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
import com.jiedaoqian.android.models.ProductHotInfo;
import com.jiedaoqian.android.utils.Common;
import com.jiedaoqian.android.utils.SettingUtil;
import com.jiedaoqian.android.utils.httputils.HttpUtil;

import java.util.List;

/**
 * Created by zenghui on 2018/4/12.
 */

public class ScanHistoryAdapter extends LoadMoreAdapter{
    List<ProductHotInfo> mDatas;
    Context context;

    public ScanHistoryAdapter(Context context, List<ProductHotInfo> mDatas){
        this.mDatas = mDatas;
        this.context = context;
    }

    @Override
    public int getFooterViewResId() {
        return R.layout.item_footer;
    }

    @Override
    public int getFooterTextViewResId() {
        return R.id.content;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CommonRcViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new HotViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public CommonRcViewHolder getFooterViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getFooterViewResId(),parent,false);
        view.setVisibility(View.VISIBLE);
        return new LoadingHolder(view);
    }

    @Override
    public void loadData(CommonRcViewHolder holder, int position) {
        HotViewHolder hotViewHolder = (HotViewHolder) holder;
        final ProductHotInfo hotInfo = mDatas.get(position);
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
                if (!TextUtils.isEmpty(hotInfo.getLoanUrl())){
                    Intent intent = new Intent(context, WebviewActivity.class);
                    intent.putExtra(Common.TITLE,hotInfo.getName());
                    intent.putExtra(Common.LINK,hotInfo.getLoanUrl());
                    context.startActivity(intent);
                }

            }
        });
        if (position == mDatas.size() - 1){
            hotViewHolder.line.setVisibility(View.GONE);
        }else {
            hotViewHolder.line.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(HttpUtil.BASE_URL+hotInfo.getIconImageUrl()).into(hotViewHolder.imgUrl);
    }



    @Override
    public void loadMore() {
        setLoadMoreText("没有更多了");
    }

    class LoadingHolder extends CommonRcViewHolder{

        View rootView;
        public LoadingHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }
    }
    class HotViewHolder extends CommonRcViewHolder{
        TextView tvTags,tvReferenceAmount,tvFundNum,tvSubTitle;
        ImageView imgUrl;
        LinearLayout starLayout;
        View rootView,line;
        public HotViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            tvTags = itemView.findViewById(R.id.tvTags);
            tvReferenceAmount = itemView.findViewById(R.id.tvReferenceAmount);
            tvFundNum = itemView.findViewById(R.id.tvFundNum);
            tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
            imgUrl = itemView.findViewById(R.id.imgUrl);
            starLayout = itemView.findViewById(R.id.starLayout);
            line = itemView.findViewById(R.id.line);


        }
    }
}
