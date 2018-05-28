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
import com.imstuding.www.handwyu.WebViewDlg.WebViewActivity;


/**
 * Created by yangkui on 2018/4/11.
 */

public class IntranetFragment extends Fragment {
    private Context mcontext;
    private View view;
    private LinearLayout layout_second_detail;
    private LinearLayout layout_empty_class;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_intranet,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        layout_empty_class=(LinearLayout) view.findViewById(R.id.layout_empty_class);
        layout_second_detail= (LinearLayout) view.findViewById(R.id.layout_second_detail);
        layout_second_detail.setOnClickListener(new MyClickListener());
        layout_empty_class.setOnClickListener(new MyClickListener());
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_second_detail: {
                    Intent intent = new Intent();
                    intent.setClass(mcontext, OtherActivity.class);
                    intent.putExtra("msg", "second");
                    startActivity(intent);
                    break;
                }
                case R.id.layout_empty_class:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,WebViewActivity.class);
                    intent.putExtra("url",UrlUtil.emptyClassUrl);
                    startActivity(intent);
                }
            }
        }
    }
}
