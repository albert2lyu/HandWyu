package com.imstuding.www.handwyu.ToolAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.CourseDetailUi.CourseDetailDlg;
import com.imstuding.www.handwyu.CourseDetailUi.CourseList;
import com.imstuding.www.handwyu.OtherUi.OtherActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.Course;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangkui on 2018//16.
 * GridView的适配器
 */
public class AbsGridAdapter extends BaseAdapter {

    private Context mContext;

    private String[][] contents;

    private int rowTotal;

    private int columnTotal;

    private int positionTotal;

    private long firstTime=0;

    private int firstposition=100;
    public AbsGridAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        return positionTotal;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        //求余得到二维索引
        int column = position % columnTotal;
        //求商得到二维索引
        int row = position / columnTotal;
        return contents[row][column];
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if( convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grib_item, null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.text);
        //如果有课,那么添加数据
        if( !getItem(position).equals("")) {
            textView.setText((String)getItem(position));
            textView.setTextColor(Color.WHITE);
            //变换颜色
            int rand = position % columnTotal;
            switch( rand ) {
                case 0:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.grid_item_bg));
                    break;
                case 1:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_12));
                    break;
                case 2:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_13));
                    break;
                case 3:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_14));
                    break;
                case 4:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_15));
                    break;
                case 5:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_16));
                    break;
                case 6:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_17));
                    break;
                case 7:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_18));
                    break;
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int row = position / columnTotal;//节数
                int column = position % columnTotal;//星期
                if (getItem(position).equals("")){
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime < 2000&&position==firstposition) {
                        Intent intent=new Intent();
                        intent.setClass(mContext,OtherActivity.class);
                        intent.putExtra("msg","add_course");
                        intent.putExtra("xq", column);
                        intent.putExtra("js", row);
                        mContext.startActivity(intent);
                    }else {
                        firstTime = secondTime;
                        firstposition=position;
                    }
                }else {
                    CourseList courseList=new CourseList(mContext,subStringToArray(contents[row][column]));
                    courseList.show();
                }
            }
        });

        return convertView;
    }

    public List<Course> subStringToArray(String string){
        List<Course> courseList=new LinkedList<>();
        String [] array=new String[5];
        String [] larray;
        larray=string.split("#");
        for (int i=0;i<larray.length;i++){
            String [] tarray;
            tarray=larray[i].split("@");
            array[0]=tarray[0];
            array[1]=tarray[1];
            array[2]="星期"+tarray[2]+"，"+"第"+tarray[3]+"小节";
            array[3]=tarray[4];
            array[4]=tarray[5];
            Course course=new Course(array[0],array[1],array[4],array[3],array[2]);
            courseList.add(i,course);
        }

        return courseList;
    }

    /**
     * 设置内容、行数、列数
     */
    public void setContent(String[][] contents, int row, int column) {
        this.contents = contents;
        this.rowTotal = row;
        this.columnTotal = column;
        positionTotal = rowTotal * columnTotal;
    }


}
