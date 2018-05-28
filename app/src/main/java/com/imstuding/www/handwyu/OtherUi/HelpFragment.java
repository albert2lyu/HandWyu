package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

/**
 * Created by yangkui on 2018/4/1.
 */

public class HelpFragment extends Fragment {

    private Context mcontext;
    private View view;
    private LinearLayout layout_operate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_help,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        layout_operate= (LinearLayout) view.findViewById(R.id.layout_operate);
        layout_operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","webView");
                    intent.putExtra("url", UrlUtil.operateUrl);
                    startActivity(intent);
            }
        });
    }
}
