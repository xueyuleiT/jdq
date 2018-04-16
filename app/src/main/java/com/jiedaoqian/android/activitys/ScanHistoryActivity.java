package com.jiedaoqian.android.activitys;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.adapters.ScanHistoryAdapter;
import com.jiedaoqian.android.models.ProductHotInfo;
import com.jiedaoqian.android.utils.Common;
import com.jiedaoqian.android.views.LoadMoreRecyclerView;

/**
 * Created by zenghui on 2018/4/12.
 */

public class ScanHistoryActivity extends BaseActivity{
    LoadMoreRecyclerView recyclerView;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_scanhistory);
        setToobar((Toolbar) findViewById(R.id.toolbar),"浏览记录");
        if (Common.scanHistory != null && Common.scanHistory.size() > 0) {
            recyclerView = findViewById(R.id.recycleView);
            recyclerView.setManager();
            ScanHistoryAdapter adapter = new ScanHistoryAdapter(this,Common.scanHistory);
            recyclerView.setLoadMoreAdapter(adapter);
        }
    }

    @Override
    public void initDatas() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
