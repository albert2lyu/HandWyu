package com.imstuding.www.handwyu.OtherDlg;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.imstuding.www.handwyu.R;

/**
 * Created by yangkui on 2018/4/6.
 */

public class ShareDlg {
    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private LinearLayout layout_share_wechat;
    private LinearLayout layout_share_qq;

    public ShareDlg(Context context){
        mcontext=context;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.choose_share, null);
        initDlg(view);
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void initDlg(View view){
        layout_share_qq= (LinearLayout) view.findViewById(R.id.layout_share_qq);
        layout_share_wechat= (LinearLayout) view.findViewById(R.id.layout_share_wechat);
        layout_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQQ();
                alertDialog.dismiss();
            }
        });

        layout_share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWechat();
                alertDialog.dismiss();
            }
        });

    }

    public void shareWechat(){
        Intent intent = new Intent(Intent.ACTION_SEND); // 地址
        ComponentName componentName = new ComponentName(
                "com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);
        intent.putExtra(Intent.EXTRA_TEXT,
                mcontext.getString(R.string.share_content));
        intent.setType("text/plain");
        mcontext.startActivity(Intent.createChooser(intent, "分享"));
    }

    public void shareQQ(){
        Intent intent = new Intent(Intent.ACTION_SEND); // 地址
        ComponentName component = new ComponentName(
                "com.tencent.mobileqq",
                "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(component);
        intent.putExtra(Intent.EXTRA_TEXT,
                mcontext.getString(R.string.share_content));
        intent.setType("text/plain");
        mcontext.startActivity(Intent.createChooser(intent, "分享"));
    }

}
