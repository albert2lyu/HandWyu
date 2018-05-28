package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangkui on 2018/3/30.
 */

public class SecondClassScoreFragment extends Fragment {

    private Context mcontext;
    private View view;
    private Button btn_second_score;
    private CheckBox keepword;
    private WebView webview;
    private EditText id;
    private EditText pwd;
    private String CookieStr;
    private MyLoadDlg myLoadDlg=null;
    private SharedPreferences sharedPreferences;
    private LinearLayout layout_second_score;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_second_score,container,false);
        initFragment(view);
        return view;
    }

    public void initData(){
        sharedPreferences=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        boolean isRemember=sharedPreferences.getBoolean("secondrememberpassword",false);
        String name=sharedPreferences.getString("second_name","");
        id.setText(name);
        if(isRemember){
            String password=sharedPreferences.getString("second_password","");
            pwd.setText(password);
            keepword.setChecked(true);
        }
    }

    public void initFragment(View view){
        layout_second_score= (LinearLayout) view.findViewById(R.id.layout_second_score);
        keepword= (CheckBox) view.findViewById(R.id.second_keeppwd);
        keepword.setChecked(true);
        id= (EditText) view.findViewById(R.id.second_id);
        pwd= (EditText) view.findViewById(R.id.second_pwd);
        initData();
        webview= (WebView) view.findViewById(R.id.test);
        WebSettings settings = webview.getSettings();

        webview.setVisibility(View.INVISIBLE);
        ViewGroup.LayoutParams lp;
        lp=webview.getLayoutParams();
        lp.width=0;
        lp.height=0;
        webview.setLayoutParams(lp);
        settings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        settings.setAllowFileAccess(true); // 允许访问文件
        webview.loadUrl(UrlUtil.secondSystemUrl);

        btn_second_score= (Button) view.findViewById(R.id.btn_second_score);
        btn_second_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("second_name",id.getText().toString());
                if(keepword.isChecked()){
                    editor.putBoolean("secondrememberpassword",true);
                    editor.putString("second_password",pwd.getText().toString());
                }else {
                    editor.remove("second_password");
                }
                editor.commit();

                myLoadDlg=new MyLoadDlg(mcontext);
                myLoadDlg.show(false);

                webview.reload();
                myTableThread1 tableThread1=new myTableThread1();
                tableThread1.start();
            }
        });

    }

    class myTableThread1 extends Thread{
        @Override
        public void run() {
            try {
                CookieManager cookieManager = CookieManager.getInstance();
                CookieStr = cookieManager.getCookie(UrlUtil.secondCookieUrl);
                String code=CookieStr.substring(CookieStr.length()-4,CookieStr.length());
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.secondLoginUrl,"post");
                httpHelp.setHeader("Cookie",CookieStr);
                httpHelp.setHeader("Host","jwc.wyu.edu.cn");
                httpHelp.setHeader("Origin","http://jwc.wyu.edu.cn");
                httpHelp.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("UserCode", id.getText().toString()));
                params.add(new BasicNameValuePair("UserPwd", pwd.getText().toString()));
                params.add(new BasicNameValuePair("Validate", code));
                params.add(new BasicNameValuePair("Submit", "提交"));
                HttpResponse httpResponse = httpHelp.postRequire(params);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "gb2312");

                    if (response.indexOf("您的密码强度较弱")!=-1){
                        myTableThread2 tableThread2=new myTableThread2();
                        tableThread2.start();
                    }else if(response.indexOf("登录信息不正确")!=-1){
                        Message message=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("response","登录信息不正确！");
                        message.setData(bundle);
                        message.what=1014;
                        handle.sendMessage(message);
                    }else if (response.indexOf("短时间内连续登录失败")!=-1){
                        Message message=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("response","短时间内连续登录失败，IP被锁定");
                        message.setData(bundle);
                        message.what=1014;
                        handle.sendMessage(message);
                    }else {
                        Message message=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("response","未知错误，退出app重登试试，或者等几小时再试试吧！");
                        message.setData(bundle);
                        message.what=1014;
                        handle.sendMessage(message);
                    }

                }
            } catch (Exception e) {
                myLoadDlg.dismiss();
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("response","请使用校园网！");
                message.setData(bundle);
                message.what=1014;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }

    }

    class myTableThread2 extends Thread{
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.secondScoreUrl,"get");
                httpHelp.setHeader("Cookie",CookieStr);
                httpHelp.setHeader("Referer","http://jwc.wyu.edu.cn/student/menu.asp");
                httpHelp.setHeader("Host","jwc.wyu.edu.cn");
                httpHelp.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                httpHelp.setHeader("Origin","http://jwc.wyu.edu.cn");
                HttpResponse httpResponse = httpHelp.getRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "gb2312");
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("response",response);
                    message.setData(bundle);
                    message.what=1011;
                    handle.sendMessage(message);
                }
            } catch (Exception e) {
                myLoadDlg.dismiss();
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("response","请使用校园网！");
                message.setData(bundle);
                message.what=1014;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1011: {
                    myLoadDlg.dismiss();
                    Bundle bundle=msg.getData();
                    showWebView();
                    hideLayout_second_score();

                    String response=bundle.getString("response");
                    webview.loadDataWithBaseURL(null,response,"text/html","gb2312",null);
                    break;
                }
                case 1014:{
                    myLoadDlg.dismiss();
                    Bundle bundle=msg.getData();
                    Toast.makeText(mcontext,bundle.getString("response"),Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

    };

    private void hideLayout_second_score(){
        layout_second_score.setVisibility(View.INVISIBLE);
        ViewGroup.LayoutParams lp;
        lp=layout_second_score.getLayoutParams();
        lp.width=0;
        lp.height=0;
        layout_second_score.setLayoutParams(lp);
    }
    private void showWebView(){
        webview.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams lp;
        lp=webview.getLayoutParams();
        lp.width=ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height=ViewGroup.LayoutParams.MATCH_PARENT;
        webview.setLayoutParams(lp);
    }

    @Override
    public void onDestroy() {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();
            webview.clearCache(true);
            webview.clearFormData();
            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }

}
