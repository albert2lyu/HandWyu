package com.imstuding.www.handwyu.AddCourse;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangkui on 2018/4/11.
 */

public class SubCourseFragment extends Fragment {

    private Context mcontext;
    private View view;
    private Spinner spinner_sub_kcmc;
    private Button fragment_sub_submit;
    private Button fragment_sub_cancel;
    private List<String> dataList;
    private ArrayAdapter<String> spinnerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_subcourse,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        spinner_sub_kcmc= (Spinner) view.findViewById(R.id.spinner_sub_kcmc);
        fragment_sub_submit= (Button) view.findViewById(R.id.fragment_sub_submit);
        fragment_sub_cancel=(Button) view.findViewById(R.id.fragment_sub_cancel);
        fragment_sub_cancel.setOnClickListener(new MyClickListener());
        fragment_sub_submit.setOnClickListener(new MyClickListener());
        initSpinner();
    }

    public void initSpinner(){
        dataList = new ArrayList<>();
        DatabaseHelper dbhelp=new DatabaseHelper(mcontext,"course.db",null,1);
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        Cursor cursor=db.rawQuery("select distinct kcmc from course",null);
        while (cursor.moveToNext()){
            String kcmc= cursor.getString(0);
            dataList.add(kcmc);
        }
        spinnerAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sub_kcmc.setAdapter(spinnerAdapter);
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_sub_submit:{
                    try {
                        delCourse();
                        Toast.makeText(mcontext,"删除成功啦！",Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }catch (Exception e){
                        Toast.makeText(mcontext,"删除失败，去反馈！",Toast.LENGTH_LONG).show();
                    }

                    break;
                }
                case R.id.fragment_sub_cancel:{
                    getActivity().onBackPressed();
                    break;
                }
            }
        }
    }
    public void delCourse(){
        String kcmc=spinner_sub_kcmc.getSelectedItem().toString();
        DatabaseHelper dbhelp=new DatabaseHelper(mcontext,"course.db",null,1);
        SQLiteDatabase db=dbhelp.getWritableDatabase();
        db.execSQL("delete from course where kcmc = ?",new String[]{kcmc});
    }
}
