package com.jiedaoqian.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by zenghui on 2018/4/12.
 */

public abstract class LoadMoreAdapter extends RecyclerView.Adapter<LoadMoreAdapter.CommonRcViewHolder> {
    public static final int ITEM_TYPE_FOOTER = 0;

    protected String loadMoreText = "加载更多";


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1){
            return ITEM_TYPE_FOOTER;
        }else {
            return 1;
        }
    }

    @Override
    public CommonRcViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOTER){
            return getFooterViewHolder(parent,viewType);
        }else {
            return getViewHolder(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(CommonRcViewHolder holder, int position) {
        if (getItemViewType(position) != ITEM_TYPE_FOOTER){
            loadData(holder, position);
        }else {
            TextView tv = holder.getView(getFooterTextViewResId());
            tv.setText(loadMoreText);
        }
    }

    @Override
    public int getItemCount() {
        return getCount()+1;
    }

    public void setLoadMoreText(String loadMoreText) {
        this.loadMoreText = loadMoreText;
        notifyItemChanged(getItemCount()-1);
    }

    public abstract int getFooterViewResId();
    public abstract int getFooterTextViewResId();
    public abstract int getCount();
    public abstract CommonRcViewHolder getViewHolder(ViewGroup parent, int viewType);
    public abstract CommonRcViewHolder getFooterViewHolder(ViewGroup parent, int viewType);
    public abstract void loadData(CommonRcViewHolder holder, int position);
    public abstract void loadMore();

    class CommonRcViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> views = new SparseArray<>();
        private View view;
        public CommonRcViewHolder(View itemView ) {
            super(itemView);
            view = itemView;
        }

        public <T extends View> T getView(int viewId){
            View v = views.get(viewId);

            if (v==null){
                v = view.findViewById(viewId);
                views.put(viewId, v);
            }
            return (T)v;
        }

        public <T extends View> T getViewWithLayoutParams(int viewId,ViewGroup.LayoutParams lp){
            View v = views.get(viewId);

            if (v==null){
                v = view.findViewById(viewId);
                v.setLayoutParams(lp);
                views.put(viewId,v);
            }
            return (T)v;
        }

        public CommonRcViewHolder setText(int viewId,String text){
            TextView tv = getView(viewId);
            tv.setText(text);
            return this;
        }
    }
}
