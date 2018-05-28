package com.imstuding.www.handwyu.VolunteerDlg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.imstuding.www.handwyu.OtherUi.OtherActivity;
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

public class CheckVolunteerState {
    private String session;
    private String testlogin= UrlUtil.voluteerInfUrl;
    private Context mcontext;
    public CheckVolunteerState(Context context){
        mcontext=context;
        session=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("sessionStr","123456789");
        if (!session.equals("123456789")){
            myVolunteerThread volunteerThread=new myVolunteerThread(testlogin+session);
            volunteerThread.start();
        }else {
            Intent intent=new Intent();
            intent.setClass(mcontext,OtherActivity.class);
            intent.putExtra("msg","volunteer_login");
            mcontext.startActivity(intent);
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
                Message message=new Message();
                Bundle bundle=new Bundle();
                message.setData(bundle);
                message.what=1021;
                handle.sendMessage(message);//是否登陆
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
                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putInt("result",result);
                    bundle.putString("msg",msg);
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
                    Bundle bundle=msg.getData();
                    int result = bundle.getInt("result");
                    if (result==1){
                        Intent intent=new Intent();
                        intent.setClass(mcontext,OtherActivity.class);
                        intent.putExtra("msg","volunteer_login");
                        mcontext.startActivity(intent);
                    }else {
                        Intent intent=new Intent();
                        intent.setClass(mcontext,OtherActivity.class);
                        intent.putExtra("msg","volunteer");
                        mcontext.startActivity(intent);
                    }
                    break;
                }
                case 1021:{
                    Toast.makeText(mcontext,"没有网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }

    };
}
