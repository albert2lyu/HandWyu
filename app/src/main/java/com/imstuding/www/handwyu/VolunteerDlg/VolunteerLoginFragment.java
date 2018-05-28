package com.imstuding.www.handwyu.VolunteerDlg;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.OtherUi.OtherActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by yangkui on 2018/4/7.
 */

public class VolunteerLoginFragment extends Fragment {
    private Context mcontext;
    private View view;
    private TextView volunteer_id;
    private TextView volunteer_pwd;
    private Button btn_volunteer_login;
    private MyLoadDlg myLoadDlg;
    private String url= UrlUtil.voluteerLoginUrl;
    private String p_name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.volunteer_login,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        btn_volunteer_login= (Button) view.findViewById(R.id.btn_volunteer_login);
        volunteer_id= (TextView) view.findViewById(R.id.volunteer_id);
        volunteer_pwd= (TextView) view.findViewById(R.id.volunteer_pwd);

        btn_volunteer_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd=volunteer_pwd.getText().toString();
                boolean isMatch= Pattern.matches("^[a-zA-Z_0-9]+$", pwd);

                if (isMatch){
                    myLoadDlg=new MyLoadDlg(mcontext);
                    myLoadDlg.show();
                    myVolunteerLoginThread volunteerLoginThread=new myVolunteerLoginThread(url);
                    volunteerLoginThread.start();
                }else {
                    Toast.makeText(mcontext,"密码必须为26个字母+数字组成，手机端必须这样，要不然没法登录！",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    class myVolunteerLoginThread extends Thread{
        private String url;

        public myVolunteerLoginThread(String url){
            this.url=url+"?UserId="+volunteer_id.getText().toString()
            +"&Password="+volunteer_pwd.getText().toString();

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
                    String sessionStr=jsonObject.getString("sessionStr");
                    String avatar=jsonObject.getString("avatar");
                    String mid=jsonObject.getString("mid");
                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putInt("result",result);
                    bundle.putString("msg",msg);
                    bundle.putString("sessionStr",sessionStr);
                    bundle.putString("avatar",avatar);
                    bundle.putString("mid",mid);

                    message.setData(bundle);
                    message.what=1021;
                    handle.sendMessage(message);//是否登陆
                }
            } catch (Exception e) {
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putInt("result",1);
                bundle.putString("msg","密码错误或者账号不存在！");
                message.setData(bundle);
                message.what=1021;
                handle.sendMessage(message);//是否登陆
                e.printStackTrace();
            }
        }
    }

    class myVolunteerPhotoThread extends Thread{
        private String url;

        public myVolunteerPhotoThread(String url){
            this.url=url;

        }

        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(url,"get");

                HttpResponse httpResponse = httpHelp.getRequire(null);

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    byte[] bytes;
                    bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putByteArray("photo",bytes);
                    message.setData(bundle);
                    message.what=1022;
                    handle.sendMessage(message);//获取验证码
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1021: {//判断是否登陆
                    myLoadDlg.dismiss();
                    Bundle bundle=msg.getData();
                    int result = bundle.getInt("result");
                    if (result==1){
                        Toast.makeText(mcontext,bundle.getString("msg"),Toast.LENGTH_SHORT).show();
                    }else {
                        String p_url=bundle.getString("avatar");
                        p_name=bundle.getString("mid");
                        myVolunteerPhotoThread volunteerPhotoThread=new myVolunteerPhotoThread(p_url);
                        volunteerPhotoThread.start();
                        String sessionStr=bundle.getString("sessionStr");
                        mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putString("sessionStr",sessionStr).commit();
                        mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putString("v_photo",p_name).commit();
                        Intent intent=new Intent();
                        intent.setClass(mcontext,OtherActivity.class);
                        intent.putExtra("msg","volunteer");
                        startActivity(intent);
                        getActivity().onBackPressed();
                    }
                    break;
                }
                case 1022:{
                    Bundle bundle=msg.getData();
                    byte bytes[]=bundle.getByteArray("photo");
                    Bitmap bmVerifation = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    try {
                        File file = new File(mcontext.getFilesDir(), "/"+ p_name);
                        FileOutputStream out = new FileOutputStream(file);
                        bmVerifation.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

        }

    };

}
