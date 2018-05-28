package com.imstuding.www.handwyu.SchoolCard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.CardInf;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.imstuding.www.handwyu.ToolUtil.MyHttpsUtil.getCurrentTime2pasword;
import static com.imstuding.www.handwyu.ToolUtil.MyHttpsUtil.getNewHttpClient;

/**
 * Created by yangkui on 2018/4/11.
 */

public class CardMeDetailDlg {
    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private CardInf m_CardInf;
    private Button btn_me_find;
    private TextView T_kanum;
    private TextView T_creattime;
    private TextView T_describeInfo;
    private TextView T_phone;
    private TextView T_style;
    public CardMeDetailDlg(Context context, CardInf cardInf){
        mcontext=context;
        m_CardInf=cardInf;
    }
    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.card_me, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void initDlg(View view){
        T_creattime=(TextView) view.findViewById(R.id.card_me_time);
        T_kanum= (TextView) view.findViewById(R.id.card_me_kanum);
        T_describeInfo= (TextView) view.findViewById(R.id.card_me_dsc);
        T_phone= (TextView) view.findViewById(R.id.card_me_phone);
        T_style= (TextView) view.findViewById(R.id.card_me_type);

        T_creattime.setText(m_CardInf.getCreattime());
        T_kanum.setText(m_CardInf.getKanum());
        T_describeInfo.setText(m_CardInf.getDescribeInfo());
        T_phone.setText(m_CardInf.getPhone());
        if (m_CardInf.getStyle()=="0"){
            T_style.setText("有人捡到");
        }else {
            T_style.setText("饭卡丢失");
        }

        btn_me_find=(Button) view.findViewById(R.id.btn_me_find);
        btn_me_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("name","123456");
                myFindCardThread findCardThread =new myFindCardThread("https://www.hxhzswz.xin/updateKaInfoJieKou.do",id);
                findCardThread.start();
            }
        });
    }

    //找到饭卡，取消丢卡信息
    class myFindCardThread extends Thread{
        private String url;
        private String kanum;
        public myFindCardThread(String url ,String kanum){
            this.url=url;
            this.kanum=kanum;
        }

        @Override
        public void run() {
            try {
                HttpClient httpClient = getNewHttpClient();
                HttpPost httpPost = new HttpPost(url);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sid", kanum));
                params.add(new BasicNameValuePair("assecc_token", getCurrentTime2pasword()));
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
                    String text=jsonObject.getString("text");
                    String data=jsonObject.getString("data");

                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("text",text);
                    bundle.putString("data",data);

                    message.setData(bundle);
                    message.what=1027;
                    handle.sendMessage(message);//更新
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
                case 1027: {//找到饭卡，取消丢卡信息
                    SchoolCardActivity schoolCardActivity= (SchoolCardActivity) mcontext;
                    schoolCardActivity.updateList();
                    Toast.makeText(mcontext,"恭喜你找回饭卡！",Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    break;
                }
            }
        }

    };
}
