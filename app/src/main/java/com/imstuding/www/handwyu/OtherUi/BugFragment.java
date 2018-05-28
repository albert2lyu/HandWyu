package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
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
import java.util.List;

/**
 * Created by yangkui on 2018/4/1.
 */

public class BugFragment extends Fragment {

    private EditText edit_bug;
    private EditText edit_phone_bug;
    private Button  btn_bug;
    private Button  btn_join;
    private Context mcontext;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_bug,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        edit_bug= (EditText) view.findViewById(R.id.edit_bug);
        edit_phone_bug= (EditText) view.findViewById(R.id.edit_contact_bug);
        btn_bug=(Button)view.findViewById(R.id.btn_bug);
        btn_join=(Button)view.findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(joinQQGroup("2tIr7v8Jda20IMs4wL6kDMliiiTi-NHo")) {
                   //Toast.makeText(mcontext,"正在跳转，稍等！",Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(mcontext,"请检查你的手机是否安装QQ",Toast.LENGTH_SHORT).show();
               }
            }
        });

        btn_bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String problem=edit_bug.getText().toString();
                String phone=edit_phone_bug.getText().toString();
                if (!problem.isEmpty()&&!phone.isEmpty()){
                    myBugReportThread reportThread=new myBugReportThread(problem,phone);
                    reportThread.start();
                }else {
                    Toast.makeText(mcontext,"*为必填项",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /****************
     *
     * 发起添加群流程。群号：掌上邑大反馈群(361011584) 的 key 为： 2tIr7v8Jda20IMs4wL6kDMliiiTi-NHo
     * 调用 joinQQGroup(2tIr7v8Jda20IMs4wL6kDMliiiTi-NHo) 即可发起手Q客户端申请加群 掌上邑大反馈群(361011584)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    class myBugReportThread extends Thread {
        private String problem;
        private String phone;
        myBugReportThread(String problem,String phone){
            this.phone=phone;
            this.problem=problem;
        }

        @Override
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(UrlUtil.bugUrl);
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("problem", problem));
                params.add(new BasicNameValuePair("phone", phone));
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
                    message.what=1012;
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
                case 1012:{
                    Bundle bundle=msg.getData();
                    if (bundle.getString("ret").equals("true")){
                        edit_bug.setText("");
                        edit_phone_bug.setText("");
                        Toast.makeText(mcontext,"谢谢你的反馈，我们会尽快解决。",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mcontext,"哪里出了问题，你好像没有提交成功。",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }

        }

    };
}
