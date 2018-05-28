package com.imstuding.www.handwyu.OtherUi;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.imstuding.www.handwyu.MainUi.TableFragment.getWeekOfDate;

/**
 * Created by yangkui on 2018/3/30.
 */

public class SetCurrentZc {

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Context mcontext;
    private Button btn_setzc;
    private String zc;
    private String xq;
    private String rq;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> dataList;
    public SetCurrentZc(Context context){
        this.mcontext=context;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.setzc_alterdlg, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }

    //填充周spinner信息
    public void fillWeekDataList() {
        dataList = new ArrayList<>();
        for(int i = 1; i < 25; i++) {
            dataList.add("第" + i + "周");
        }
    }

    public void initDlg(View view){
        spinner= (Spinner) view.findViewById(R.id.spinner_setWeek);
        fillWeekDataList();
        spinnerAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        btn_setzc= (Button) view.findViewById(R.id.btn_setweek);
        btn_setzc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbhelp=new DatabaseHelper(mcontext,"course.db",null,1);
                SQLiteDatabase db=dbhelp.getWritableDatabase();

                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String rq=sdf.format(d);
                String s_zc=spinner.getSelectedItem().toString();
                s_zc=s_zc.substring(1,s_zc.length()-1);
                String xq=getWeekOfDate(d);
                db.execSQL("insert into week values(?,?,?)",new String[]{xq,rq,s_zc});
                Toast.makeText(mcontext,"已成功设置 第"+s_zc+"周 为当前周",Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }
}
