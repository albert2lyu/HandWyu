package com.imstuding.www.handwyu.SchoolCard;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.CardInf;

/**
 * Created by yangkui on 2018/4/10.
 */

public class CardDetailDlg {
    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private CardInf cardInf;
    private TextView T_kanum;
    private TextView T_creattime;
    private TextView T_describeInfo;
    private TextView T_phone;
    private TextView T_style;
    public CardDetailDlg(Context mcontext, CardInf cardInf){
        this.mcontext=mcontext;
        this.cardInf=cardInf;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.card_detail, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void initDlg(View view){
        try{
            T_kanum= (TextView) view.findViewById(R.id.card_kanum);
            T_creattime= (TextView) view.findViewById(R.id.card_time);
            T_describeInfo= (TextView) view.findViewById(R.id.card_dsc);
            T_phone= (TextView) view.findViewById(R.id.card_phone);
            T_style= (TextView) view.findViewById(R.id.card_type);

            T_kanum.setText(cardInf.getKanum());
            T_creattime.setText(cardInf.getCreattime());
            T_describeInfo.setText(cardInf.getDescribeInfo());
            T_phone.setText(cardInf.getPhone());
            if (cardInf.getStyle()=="0"){
                T_style.setText("有人捡到");
            }else {
                T_style.setText("饭卡丢失");
            }

        }catch (Exception e){
            T_kanum.setText("NULL");
            T_creattime.setText("NULL");
            T_describeInfo.setText("NULL");
            T_phone.setText("NULL");
            T_style.setText("NULL");
        }


    }
}
