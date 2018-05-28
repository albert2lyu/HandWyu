package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.imstuding.www.handwyu.OtherDlg.PayDlg;
import com.imstuding.www.handwyu.OtherDlg.ShareDlg;
import com.imstuding.www.handwyu.R;

import java.net.URLEncoder;

/**
 * Created by yangkui on 2018/4/1.
 */

public class MoreFragment extends Fragment {

    private Context mcontext;
    private View view;
    private LinearLayout layout_help;
    private LinearLayout layout_about;
    private LinearLayout layout_bug;
    private LinearLayout layout_pay;
    private LinearLayout layout_share;
    private LinearLayout layout_redpaper;
    private LinearLayout layout_update_explain;
    private LinearLayout layout_auto_update;
    private Switch auto_update_switch;
    private SharedPreferences sharedPreferences;
    private boolean autoUpdate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_more,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        auto_update_switch= (Switch) view.findViewById(R.id.auto_update_switch);
        layout_auto_update=(LinearLayout) view.findViewById(R.id.layout_auto_update);
        layout_help= (LinearLayout) view.findViewById(R.id.layout_help);
        layout_about= (LinearLayout) view.findViewById(R.id.layout_about);
        layout_bug= (LinearLayout) view.findViewById(R.id.layout_bug);
        layout_pay=(LinearLayout) view.findViewById(R.id.layout_pay);
        layout_share=(LinearLayout) view.findViewById(R.id.layout_share);
        layout_redpaper=(LinearLayout) view.findViewById(R.id.layout_redpaper);
        layout_update_explain=(LinearLayout) view.findViewById(R.id.layout_update_explain);

        boolean redPaper=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("redPaper",false);
        autoUpdate=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("autoUpdate",true);
        if (!redPaper){
            hideRedPaper();
        }
        if (autoUpdate){
            auto_update_switch.setChecked(true);
        }else {
            auto_update_switch.setChecked(false);
        }

        layout_help.setOnClickListener(new MyClickListener());
        layout_about.setOnClickListener(new MyClickListener());
        layout_bug.setOnClickListener(new MyClickListener());
        layout_pay.setOnClickListener(new MyClickListener());
        layout_share.setOnClickListener(new MyClickListener());
        layout_redpaper.setOnClickListener(new MyClickListener());
        layout_update_explain.setOnClickListener(new MyClickListener());
        layout_auto_update.setOnClickListener(new MyClickListener());
    }

    public void hideRedPaper(){
        ViewGroup.LayoutParams lp;
        lp=layout_redpaper.getLayoutParams();
        lp.width=0;
        lp.height=0;
        layout_redpaper.setLayoutParams(lp);
        layout_redpaper.setVisibility(View.INVISIBLE);
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_help:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","help");
                    startActivity(intent);
                    break;
                }
                case R.id.layout_about:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","about");
                    startActivity(intent);
                    break;
                }
                case R.id.layout_bug:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","bug");
                    startActivity(intent);
                    break;
                }
                case R.id.layout_pay:{
                    PayDlg payDlg=new PayDlg(mcontext);
                    payDlg.showPay();
                    break;
                }
                case R.id.layout_redpaper:{
                    PayDlg payDlg=new PayDlg(mcontext);
                    payDlg.showRedPaper();
                    break;
                }
                case R.id.layout_share:{
                    ShareDlg shareDlg=new ShareDlg(mcontext);
                    shareDlg.show();
                    break;
                }
                case R.id.layout_update_explain:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","update_explain");
                    startActivity(intent);
                    break;
                }
                case R.id.layout_auto_update:{
                    if (auto_update_switch.isChecked()){
                        mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putBoolean("autoUpdate",false).commit();
                        auto_update_switch.setChecked(false);
                    }else {
                        mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putBoolean("autoUpdate",true).commit();
                        auto_update_switch.setChecked(true);
                    }

                    break;
                }
        }
        }
    }
}
