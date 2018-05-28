package com.imstuding.www.handwyu.SchoolCard;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.CardInf;
import com.imstuding.www.handwyu.WebViewDlg.LoginActivity;

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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.imstuding.www.handwyu.ToolUtil.MyHttpsUtil.getCurrentTime2pasword;
import static com.imstuding.www.handwyu.ToolUtil.MyHttpsUtil.getNewHttpClient;

public class SchoolCardActivity extends AppCompatActivity {

    private Button btn_add;
    private Button btn_me;
    private SimpleAdapter simpleAdapter;
    private ListView list_card;
    private List<CardInf> cardInfList;
    private CardInf m_CardInf;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private EditText E_kanum;
    private EditText E_describeInfo;
    private EditText E_phone;
    private RadioGroup R_style;
    private Button btn_submit;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_card);
        initActivity();
    }
    public void initActivity(){
        cardInfList=new LinkedList<>();
        list_card= (ListView) findViewById(R.id.school_card_list);
        list_card.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardDetailDlg cardDetailDlg=new CardDetailDlg(SchoolCardActivity.this,cardInfList.get(position));
                cardDetailDlg.show();
            }
        });
        myALLCardThread allCardThread = new myALLCardThread("https://www.hxhzswz.xin/getKaInfoJieKou.do");
        allCardThread.start();

        btn_add= (Button) findViewById(R.id.school_card_add);
        btn_me= (Button) findViewById(R.id.school_card_me);
        btn_add.setOnClickListener(new MyClickListener());
        btn_me.setOnClickListener(new MyClickListener());
    }

    public void show(){
        builder=new AlertDialog.Builder(SchoolCardActivity.this);
        LayoutInflater inflater =getLayoutInflater();
        View view = inflater.inflate(R.layout.add_card_inf, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void initDlg(View view){
        E_kanum= (EditText) view.findViewById(R.id.add_card_id);
        E_describeInfo= (EditText) view.findViewById(R.id.add_card_dsc);
        E_phone= (EditText) view.findViewById(R.id.add_card_phone);
        R_style= (RadioGroup) view.findViewById(R.id.add_card_radio);
        btn_submit=(Button) view.findViewById(R.id.add_card_submit);
        btn_cancel=(Button) view.findViewById(R.id.add_card_cancel);
        btn_submit.setOnClickListener(new MyClickListener());
        btn_cancel.setOnClickListener(new MyClickListener());

    }

    //添加丢饭卡信息
    class myAddCardThread extends Thread{
        private String url;
        private String kanum;
        private String describe;
        private String callme;
        private String userTpye;
        public myAddCardThread(String url,String kanum,String describe,String callme,String userTpye){
            this.url=url;
            this.kanum=kanum;
            this.describe=describe;
            this.callme=callme;
            this.userTpye=userTpye;
        }

        @Override
        public void run() {
            try {
                HttpClient httpClient = getNewHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Connection", "keep-alive");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("kanum", kanum));
                params.add(new BasicNameValuePair("describe", describe));
                params.add(new BasicNameValuePair("callme", callme));
                params.add(new BasicNameValuePair("userTpye", userTpye));
                params.add(new BasicNameValuePair("time", ""));
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
                    message.what=1025;
                    handle.sendMessage(message);//更新
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //返回所有丢卡信息
    class myALLCardThread extends Thread{
        private String url;

        public myALLCardThread(String url){
            this.url=url;
        }

        @Override
        public void run() {
            try {
                HttpClient httpClient = getNewHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Connection", "keep-alive");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userTpye", "2"));
                //params.add(new BasicNameValuePair("isfind", "0"));
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
                Message message=new Message();
                message.what=1021;
                handle.sendMessage(message);//有么有网判断
                e.printStackTrace();
            }
        }


        private void parseJSONWithJSONObject(String jsonData) {
            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String text=jsonObject.getString("text");
                JSONArray jsonArray1= jsonObject.getJSONArray("data");
                for (int i=0;i < jsonArray1.length(); i++){
                    JSONObject jsonObject1= jsonArray1.getJSONObject(i);
                    String kanum= jsonObject1.getString("kanum");
                    String userType=  jsonObject1.getString("userType");
                    String creattime= jsonObject1.getString("creattime");
                    String describeInfo= jsonObject1.getString("describeInfo");
                    String callme= jsonObject1.getString("callme");
                    cardInfList.add(new CardInf(kanum,creattime,describeInfo,callme,userType));
                }

                //把数据发送出去
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("text",text);

                message.setData(bundle);
                message.what=1026;
                handle.sendMessage(message);//更新
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //检查我是否丢卡
    class myCheckCardThread extends Thread{
        private String url;
        private String kanum;
        public myCheckCardThread(String url,String kanum){
            this.url=url;
            this.kanum=kanum;
        }

        @Override
        public void run() {
            try {
                HttpClient httpClient = getNewHttpClient();
                HttpPost httpPost = new HttpPost(url);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userId", kanum));
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
                    if (text.equals("1000")){
                        JSONArray jsonArray1= jsonObject.getJSONArray("data");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        String kanum=jsonObject1.getString("kanum");
                        String describeInfo=jsonObject1.getString("describeInfo");
                        String callme=jsonObject1.getString("callme");
                        String createtime=jsonObject1.getString("creattime");
                        String userType=jsonObject1.getString("userType");
                        m_CardInf=new CardInf(kanum,createtime,describeInfo,callme,userType);
                    }

                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("text",text);
                    message.setData(bundle);
                    message.what=1028;
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
                case 1025: {//添加饭卡丢失信息
                    Bundle bundle =msg.getData();
                    String data=bundle.getString("data");
                    if (data.equals("1")){
                        Toast.makeText(SchoolCardActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        updateList();
                    }else if (data.equals("-1001")){
                        Toast.makeText(SchoolCardActivity.this,"发布失败，请检查学号格式是否有误！",Toast.LENGTH_SHORT).show();
                    }else if(data.equals("-2000")){
                        Toast.makeText(SchoolCardActivity.this,"已经有人发布，请勿重复发布，谢谢！",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SchoolCardActivity.this,"未知错误！请检查网络连接！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case 1026: {//全部饭卡丢失信息
                    List<Map<String,String>> data=new ArrayList<Map<String,String>>();
                    for (int i=0;i<cardInfList.size();i++){
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("kanum",cardInfList.get(i).getKanum());
                        if (cardInfList.get(i).getStyle().equals("0")){
                            map.put("style","有人捡到-->点击查看详情");
                        }else {
                            map.put("style","饭卡丢失-->点击查看详情");
                        }
                        map.put("phone","联系方式："+cardInfList.get(i).getPhone());
                        data.add(map);
                    }
                    setOrUpdateSimpleAdapter(data);
                    break;
                }
                case 1028: {//检查我是否丢失饭卡
                    Bundle bundle =msg.getData();
                    String text=bundle.getString("text");
                    if (text.equals("1000")){
                        CardMeDetailDlg cardMeDetailDlg=new CardMeDetailDlg(SchoolCardActivity.this,m_CardInf);
                        cardMeDetailDlg.show();
                    }else if (text.equals("2000")){
                        Toast.makeText(SchoolCardActivity.this,"暂时没有人发布找到您的饭卡！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case 1021:{
                    Toast.makeText(SchoolCardActivity.this,"没有网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

    };

    public void  updateList(){
        cardInfList=new LinkedList<>();
        myALLCardThread allCardThread = new myALLCardThread("https://www.hxhzswz.xin/getKaInfoJieKou.do");
        allCardThread.start();
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.school_card_add:{
                    show();
                    break;
                }
                case R.id.school_card_me:{
                    boolean flag=getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("isLogin",false);
                    if (flag){
                        String id=getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("name","123456");
                        myCheckCardThread checkCardThread =new myCheckCardThread("https://www.hxhzswz.xin/getAKaInfoBySIdJieKou.do",id);
                        checkCardThread.start();
                    }else {
                        Toast.makeText(SchoolCardActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(SchoolCardActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
                case R.id.add_card_submit:{
                    int t_flag=0;
                    if (R_style.getCheckedRadioButtonId()==R.id.add_card_find){
                        t_flag=0;
                    }else if(R_style.getCheckedRadioButtonId()==R.id.add_card_lose){
                        t_flag=1;
                    }
                    myAddCardThread addCardThread = new myAddCardThread("https://www.hxhzswz.xin/addKaInfoJieKou.do",E_kanum.getText().toString(),
                            E_describeInfo.getText().toString(),E_phone.getText().toString(),t_flag+"");
                    addCardThread.start();
                    break;
                }
                case R.id.add_card_cancel:{
                    alertDialog.dismiss();
                    break;
                }
            }
        }
    }

    public void setOrUpdateSimpleAdapter(List<Map<String,String>> data){
        simpleAdapter=new SimpleAdapter(getApplicationContext(),data,R.layout.list_card_item,
                new String[]{"kanum","style","phone"},new int[]{R.id.item_card_num,R.id.item_card_style,R.id.item_card_phone});
        list_card.setAdapter(simpleAdapter);
    }
}
