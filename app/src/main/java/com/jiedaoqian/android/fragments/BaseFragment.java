package com.jiedaoqian.android.fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;


import es.dmoral.toasty.Toasty;

/**
 * Created by zenghui on 2017/6/27.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void showWaringToast(String s){
        Toasty.warning(getActivity(), s, Toast.LENGTH_SHORT, true).show();
    }
    public void showToast(String s){
        Toasty.info(getActivity(), s, Toast.LENGTH_SHORT, true).show();
    }
    public void showSuccessToast(String s){
        Toasty.success(getActivity(), s, Toast.LENGTH_SHORT, true).show();
    }

    public void getDatas(){

    }
}


