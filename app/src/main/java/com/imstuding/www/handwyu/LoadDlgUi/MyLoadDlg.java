package com.imstuding.www.handwyu.LoadDlgUi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;

/**
 * Created by yangkui on 2018/3/24.
 */
public class MyLoadDlg {
    private Context mcontext;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    private long firstTime = 0;
    public MyLoadDlg(Context mcontext){
        this.mcontext=mcontext;
    }
    public void show(){
        LoadingView mLoadView;
        AlertDialog.Builder builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.loading, null);
        mLoadView=(LoadingView)view.findViewById(R.id.gsv) ;
        mLoadView.setStatue(LoadingView.LOADING);


        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        WindowManager.LayoutParams lp=alertDialog.getWindow().getAttributes();
        lp.alpha=1.0f;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.setView(view);
        alertDialog.show();
        WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        p.height = (int) (dm.heightPixels*0.19);
        p.width = (int) (dm.widthPixels*0.2);
        alertDialog.getWindow().setAttributes(p);     //设置生效
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    public void show(boolean flag){
        final LoadingView mLoadView;
        AlertDialog.Builder builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.loading, null);
        mLoadView=(LoadingView)view.findViewById(R.id.gsv) ;
        mLoadView.setStatue(LoadingView.LOADING);


        builder.setView(view);
        builder.setCancelable(flag);
        alertDialog=builder.create();
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) {
                        Toast.makeText(mcontext, "请不要退出，再等等，网络比较慢！", Toast.LENGTH_SHORT).show();
                        firstTime = secondTime;
                        return false;
                    } else {
                        dismiss();
                    }
                }
                return false;
            }
        });

        WindowManager.LayoutParams lp=alertDialog.getWindow().getAttributes();
        lp.alpha=1.0f;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.setView(view);
        alertDialog.show();
        WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        p.height = (int) (dm.heightPixels*0.19);
        p.width = (int) (dm.widthPixels*0.2);
        alertDialog.getWindow().setAttributes(p);     //设置生效
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    public void dismiss(){
        try {
            if (alertDialog.isShowing())
                alertDialog.dismiss();
        }catch (Exception e){

        }

    }
    public boolean isShowing(){
        boolean flag=false;
        try {
            flag= alertDialog.isShowing();
        }catch (Exception e){
        }
        return flag;
    }
}