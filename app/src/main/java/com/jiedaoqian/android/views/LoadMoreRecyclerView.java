package com.jiedaoqian.android.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.jiedaoqian.android.adapters.LoadMoreAdapter;

/**
 * Created by zenghui on 2018/4/12.
 */

public class LoadMoreRecyclerView extends RecyclerView {

    private LinearLayoutManager mLinearLayoutManager;
    private LoadMoreAdapter mAdapter;

    public LoadMoreRecyclerView(Context context) {
        this(context,null);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        int endCompletelyPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        if (endCompletelyPosition == mAdapter.getItemCount()-1){
            post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.loadMore();
                }
            });
        }
    }

    public void setManager(){
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(mLinearLayoutManager);
    }

    public LinearLayoutManager getLayoutManager() {
        return mLinearLayoutManager;
    }

    public void setLoadMoreAdapter(LoadMoreAdapter mAdapter) {
        this.mAdapter = mAdapter;
        setAdapter(mAdapter);
    }
}
