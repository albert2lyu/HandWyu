package com.imstuding.www.handwyu.VolunteerDlg;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.OtherUi.OtherActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yangkui on 2018/4/7.
 */

public class VolunteerFragment extends Fragment {
    private Context mcontext;
    private TextView v_realname;
    private TextView v_deptname;
    private TextView v_cardno;
    private LinearLayout layout_volunteer;
    private LinearLayout layout_volunteer_time;
    private LinearLayout layout_volunteer_history;
    private LinearLayout layout_volunteer_logout;
    private LinearLayout layout_volunteer_download;
    private ImageView v_photo;
    private MyLoadDlg myLoadDlg;
    private View view;
    private String session;
    private String testlogin=UrlUtil.voluteerInfUrl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_volunteer,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        v_photo= (ImageView) view.findViewById(R.id.v_photo);
        v_realname= (TextView) view.findViewById(R.id.v_realname);
        v_deptname= (TextView) view.findViewById(R.id.v_deptname);
        v_cardno= (TextView) view.findViewById(R.id.v_cardno);
        myLoadDlg=new MyLoadDlg(mcontext);
        myLoadDlg.show();
        layout_volunteer_download=(LinearLayout) view.findViewById(R.id.layout_volunteer_download);
        layout_volunteer_logout=(LinearLayout) view.findViewById(R.id.layout_volunteer_logout);
        layout_volunteer_time=(LinearLayout) view.findViewById(R.id.layout_volunteer_time);
        layout_volunteer_history=(LinearLayout) view.findViewById(R.id.layout_volunteer_history);
        layout_volunteer_time.setOnClickListener(new MyClickListener());
        layout_volunteer_history.setOnClickListener(new MyClickListener());
        layout_volunteer_logout.setOnClickListener(new MyClickListener());
        layout_volunteer_download.setOnClickListener(new MyClickListener());

        layout_volunteer= (LinearLayout) view.findViewById(R.id.layout_volunteer);
        layout_volunteer.setVisibility(View.INVISIBLE);
        session=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("sessionStr","123456789");
        myVolunteerThread volunteerThread=new myVolunteerThread(testlogin+session);
        volunteerThread.start();
    }

    public void setPhoto(String photo){
        try {
            String path=mcontext.getFilesDir().getPath();
            Bitmap bmVerifation= BitmapFactory.decodeFile(path+"/"+ photo);
            if (bmVerifation==null){

            }else {
                v_photo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                v_photo.setImageBitmap(bmVerifation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class myVolunteerThread extends Thread{
        private String url;

        public myVolunteerThread(String url){
            this.url=url;
        }

        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(url,"get");
                HttpResponse httpResponse = httpHelp.getRequire(null);

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    parseJSONWithJSONObject(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void parseJSONWithJSONObject(String jsonData) {
            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int result=jsonObject.getInt("result");
                    String msg=jsonObject.getString("msg");
                    JSONArray jsonArray1= jsonObject.getJSONArray("data");
                    JSONObject jsonObject1= jsonArray1.getJSONObject(i);
                    String RealName=jsonObject1.getString("RealName");
                    String DeptName=jsonObject1.getString("DeptName");
                    String CertificatesPhoto=jsonObject1.getString("CertificatesPhoto");
                    String GyCardNo=jsonObject1.getString("GyCardNo");
                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putInt("result",result);
                    bundle.putString("msg",msg);
                    bundle.putString("RealName",RealName);
                    bundle.putString("DeptName",DeptName);
                    bundle.putString("CertificatesPhoto",CertificatesPhoto);
                    bundle.putString("GyCardNo",GyCardNo);
                    message.setData(bundle);
                    message.what=1020;
                    handle.sendMessage(message);//是否登陆
                }
            } catch (Exception e) {
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putInt("result",1);
                bundle.putString("msg","密码错误或者账号不存在！");
                message.setData(bundle);
                message.what=1020;
                handle.sendMessage(message);//是否登陆
                e.printStackTrace();
            }
        }
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1020: {//判断是否登陆
                    myLoadDlg.dismiss();
                    layout_volunteer.setVisibility(View.VISIBLE);
                    Bundle bundle=msg.getData();
                    int result = bundle.getInt("result");
                    if (result==1){
                        Intent intent=new Intent();
                        intent.setClass(mcontext,OtherActivity.class);
                        intent.putExtra("msg","volunteer_login");
                        startActivity(intent);
                    }else {
                        v_realname.setText(bundle.getString("RealName"));
                        v_deptname.setText("所属机构："+bundle.getString("DeptName"));
                        v_cardno.setText("义工编号："+bundle.getString("GyCardNo"));
                        String v_name=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("v_photo","123456789");
                        setPhoto(v_name);

                        v_realname.invalidate();
                        v_deptname.invalidate();
                        v_cardno.invalidate();
                        v_photo.invalidate();
                    }
                    break;
                }
            }

        }

    };

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_volunteer_history:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","webView");
                    intent.putExtra("url", UrlUtil.volunteerHistoryUrl+session);
                    startActivity(intent);
                    break;
                }
                case R.id.layout_volunteer_time:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","webView");
                    intent.putExtra("url",UrlUtil.volunteerTimeUrl+session);
                    startActivity(intent);
                    break;
                }
                case R.id.layout_volunteer_logout:{
                    mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putString("sessionStr","123456789").commit();
                    CheckVolunteerState checkVolunteerState=new CheckVolunteerState(mcontext);
                    getActivity().onBackPressed();
                    break;
                }
                case R.id.layout_volunteer_download:{
                    showSimpleDialog(UrlUtil.volunteerAppUrl);
                    break;
                }
            }
        }
    }

    private void showSimpleDialog(final String appurl) {
        AlertDialog.Builder builder;
        builder=new AlertDialog.Builder(mcontext);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setMessage("是否前去下载江门义工app？");

        //监听下方button点击事件
        builder.setPositiveButton("前去下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(appurl);
                intent.setData(content_url);
                mcontext.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

}
