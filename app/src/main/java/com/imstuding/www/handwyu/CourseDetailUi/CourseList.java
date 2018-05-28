package com.imstuding.www.handwyu.CourseDetailUi;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangkui on 2018/3/30.
 */

public class CourseList {
    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private SimpleAdapter simpleAdapter;
    private ListView list_course;
    private List<Course> courseList;

    public CourseList(Context context,List<Course> courseList){
        this.courseList=courseList;
        mcontext=context;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.course_list, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }



    public void initDlg(View view){
        list_course= (ListView) view.findViewById(R.id.course_detail_list);
        List<Map<String,String>> data=new ArrayList<Map<String,String>>();
        for (int i=0;i<courseList.size();i++){
            Map<String,String> map=new HashMap<String, String>();
            map.put("kcmc",courseList.get(i).getKcmc());
            map.put("js",courseList.get(i).getJxcdmc()+"，"+courseList.get(i).getJs());
            map.put("teaxms",courseList.get(i).getTeaxms());
            data.add(map);
        }
        setOrUpdateSimpleAdapter(data);
        list_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseDetailDlg courseDetailDlg=new CourseDetailDlg(mcontext,courseList.get(position));
                courseDetailDlg.show();
            }
        });
    }

    public void setOrUpdateSimpleAdapter(List<Map<String,String>> data){
        simpleAdapter=new SimpleAdapter(mcontext,data,R.layout.list_course_item,
                new String[]{"kcmc","js","teaxms"},new int[]{R.id.item_course_kcmc,R.id.item_course_js,R.id.item_course_teaxms});
        list_course.setAdapter(simpleAdapter);
    }
}
