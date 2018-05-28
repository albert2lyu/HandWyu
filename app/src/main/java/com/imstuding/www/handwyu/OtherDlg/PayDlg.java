package com.imstuding.www.handwyu.OtherDlg;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;

import java.net.URLEncoder;

/**
 * Created by yangkui on 2018/4/5.
 */

public class PayDlg {
    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    public static final String ALIPAY_PERSON_2_PAY = "https://qr.alipay.com/FKX01918LZUJI430KWW2BB";//个人(支付宝里面我的二维码,然后提示让用的收款码)
    public static final String ALIPAY_RED_PAPER="https://qr.alipay.com/c1x06902hjuvhoub5vx5if9";

    public PayDlg(Context context){
        mcontext=context;
    }

    public void showPay(){
        builder=new AlertDialog.Builder(mcontext);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("提示");
        builder.setMessage(R.string.active_string1);

        //监听下方button点击事件
        builder.setPositiveButton(R.string.btn_active, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openAliPay2Pay(ALIPAY_PERSON_2_PAY);
            }
        });
        builder.setNegativeButton(R.string.btn_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mcontext, R.string.passive_string, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void showRedPaper(){
        builder=new AlertDialog.Builder(mcontext);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("提示");
        builder.setMessage(R.string.active_string2);

        //监听下方button点击事件
        builder.setPositiveButton(R.string.btn_active, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openAliPay2Pay(ALIPAY_RED_PAPER);
            }
        });
        builder.setNegativeButton(R.string.btn_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mcontext, R.string.passive_string, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }

    /**
     * 支付
     *
     * @param qrCode
     */
    private void openAliPay2Pay(String qrCode) {
        if (openAlipayPayPage(mcontext, qrCode)) {
            Toast.makeText(mcontext, "正在跳转，请耐心等待，谢谢。", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mcontext, "跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean openAlipayPayPage(Context context, String qrcode) {
        try {
            qrcode = URLEncoder.encode(qrcode, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            final String alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + qrcode;
            openUri(context, alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送一个intent
     *
     * @param context
     * @param s
     */
    private static void openUri(Context context, String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        context.startActivity(intent);
    }

}
