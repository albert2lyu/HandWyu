package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imstuding.www.handwyu.R;

/**
 * Created by yangkui on 2018/4/5.
 */

public class UpdateExplainFragment extends Fragment {

    private Context mcontext;
    private View view;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_explain,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){

    }
}
