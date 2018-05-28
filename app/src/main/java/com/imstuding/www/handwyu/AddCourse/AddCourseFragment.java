package com.imstuding.www.handwyu.AddCourse;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;

import static com.imstuding.www.handwyu.MainUi.TableFragment.static_term;
import static com.imstuding.www.handwyu.MainUi.TableFragment.static_zc;

/**
 * Created by yangkui on 2018/4/11.
 */

public class AddCourseFragment extends Fragment {
    private Context mcontext;
    private View view;
    private int js=0;
    private int xq=0;
    private Button btn_cancel;
    private Button btn_submit;
    private EditText e_kcmc;
    private EditText e_jxcdmc;
    private EditText e_teaxms;
    private Spinner spinner_xq;
    private Spinner spinner_js;
    private Spinner spinner_start;
    private Spinner spinner_end;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_add_course,container,false);
        try{
            js=getArguments().getInt("js");
            xq=getArguments().getInt("xq");
        }catch (Exception e){
            js=1;
            xq=1;
        }
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        e_kcmc= (EditText) view.findViewById(R.id.fragment_add_kcmc);
        e_jxcdmc= (EditText) view.findViewById(R.id.fragment_add_jxcdmc);
        e_teaxms= (EditText) view.findViewById(R.id.fragment_add_teaxms);
        spinner_xq= (Spinner) view.findViewById(R.id.fragment_add_spinner_xq);
        spinner_js= (Spinner) view.findViewById(R.id.fragment_add_spinner_js);
        spinner_start= (Spinner) view.findViewById(R.id.fragment_add_spinner_start);
        spinner_end= (Spinner) view.findViewById(R.id.fragment_add_spinner_end);
        btn_cancel= (Button) view.findViewById(R.id.fragment_add_cancel);
        btn_submit= (Button) view.findViewById(R.id.fragment_add_submit);
        initView();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFilled())
                    insertInToDataBase();
                else
                    Toast.makeText(mcontext,"请填写完整！",Toast.LENGTH_SHORT).show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    //判断是否直接点击了添加
    public boolean isFilled(){
        String kcmc=e_kcmc.getText().toString();
        String jxcdmc=e_jxcdmc.getText().toString();

        if (kcmc.isEmpty()||jxcdmc.isEmpty())
            return false;
        return true;
    }

    //设置节和星期
    public void initView(){
        spinner_js.setSelection(js);
        spinner_xq.setSelection(xq);
        spinner_start.setSelection(static_zc-1);
        spinner_end.setSelection(static_zc-1);
    }

    public void insertInToDataBase(){
        int xq=getXq();
        int zc_s=getStartZc();
        int zc_e=getEndZc();
        String js=getJs();
        String kcmc=e_kcmc.getText().toString();
        String jxcdmc=e_jxcdmc.getText().toString();
        String teaxms=e_teaxms.getText().toString();
        if (teaxms.isEmpty())
            teaxms="unknow";
        DatabaseHelper dbhelp=new DatabaseHelper(mcontext,"course.db",null,1);
        SQLiteDatabase db=dbhelp.getWritableDatabase();
        for (int i=zc_s;i<=zc_e;i++){
            try{
                db.execSQL("insert into course values(?,?,?,?,?,?,?)",new String[]{jxcdmc,teaxms,xq+"",js,kcmc,i+"",static_term});
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(mcontext,"操作失败！",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(mcontext,"操作成功啦！",Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    public int getXq(){
        int xq=0;
        String string= spinner_xq.getSelectedItem().toString();
        switch (string){
            case "星期一":{
                xq=1;
                break;
            }
            case "星期二":{
                xq=2;
                break;
            }
            case "星期三":{
                xq=3;
                break;
            }
            case "星期四":{
                xq=4;
                break;
            }
            case "星期五":{
                xq=5;
                break;
            }
            case "星期六":{
                xq=6;
                break;
            }
            case "星期日":{
                xq=7;
                break;
            }
        }
        return xq;
    }

    public int getStartZc(){
        int zc=0;
        String string= spinner_start.getSelectedItem().toString();
        string = string.substring(1,string.length()-1);
        zc=Integer.parseInt(string);
        return zc;
    }

    public int getEndZc(){
        int zc=0;
        String string= spinner_end.getSelectedItem().toString();
        string = string.substring(1,string.length()-1);
        zc=Integer.parseInt(string);
        return zc;
    }

    public String getJs(){
        String js="0102";
        String string= spinner_js.getSelectedItem().toString();
        switch (string){
            case "第一大节":{
                js="0102";
                break;
            }
            case "第二大节":{
                js="0304";
                break;
            }
            case "第三大节":{
                js="0506";
                break;
            }
            case "第四大节":{
                js="0708";
                break;
            }
            case "第五大节":{
                js="0910";
                break;
            }
            case "第六大节":{
                js="1112";
                break;
            }
        }
        return js;
    }

}
