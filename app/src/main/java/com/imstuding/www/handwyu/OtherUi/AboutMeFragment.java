package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yangkui on 2018/4/1.
 */

public class AboutMeFragment extends Fragment {

    private Context mcontext;
    private View view;
    private TextView count_people;
    private Button btn_like;
    private int count=0;
    private TextView version_explain;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_about,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        btn_like= (Button) view.findViewById(R.id.btn_like);
        version_explain= (TextView) view.findViewById(R.id.version_explain);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        version_explain.setText("当前版本v"+getVersion()+"\n©"+year+" imstuding.com\nAll rights reserved.");
        count_people= (TextView) view.findViewById(R.id.count_like);
        count_people.setVisibility(View.INVISIBLE);
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count<=10){
                    myLikeThread likeThread=new myLikeThread();
                    likeThread.start();
                }else {
                    count_people.setText("谢谢你，不能再点啦，休息一会吧。");
                }

            }
        });
    }

    public String getVersion(){
        String localVersion = null;
        try {
            PackageInfo packageInfo = mcontext.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(mcontext.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    class myLikeThread extends Thread {

        @Override
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(UrlUtil.likeUrl);
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("count", "1"));
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = httpClient.execute(httpPost);
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
                    String ret=jsonObject.getString("ret");
                    String msg=jsonObject.getString("msg");
                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("ret",ret);
                    bundle.putString("msg",msg);
                    message.setData(bundle);
                    message.what=1013;
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
                case 1013:{
                    Bundle bundle=msg.getData();
                    if (count<=10){
                        count_people.setVisibility(View.VISIBLE);
                        count_people.setText("你是第"+bundle.getString("msg")+"个点赞者，谢谢你的支持！");
                    }else {
                        count_people.setText("谢谢你，不能再点啦，休息一会吧。");
                    }

                    break;
                }
            }

        }

    };
}
