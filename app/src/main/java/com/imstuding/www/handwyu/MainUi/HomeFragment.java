package com.imstuding.www.handwyu.MainUi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.Dictionaty.DictionaryActivity;
import com.imstuding.www.handwyu.WebViewDlg.LoginActivity;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.OtherUi.OtherActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;
import com.imstuding.www.handwyu.testUpdate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by yangkui on 2018/3/22.
 */

public class HomeFragment extends Fragment {

    private Context mcontext=null;
    private View view=null;
    private LinearLayout layout_update=null;
    private LinearLayout layout_dic=null;
    private LinearLayout layout_intranet=null;
    private LinearLayout layout_second_simple=null;
    private LinearLayout layout_logout=null;
    private LinearLayout layout_exam=null;
    private LinearLayout layout_more=null;
    private LinearLayout layout_shool_date=null;
    private ImageView home_imageview_logout=null;
    private TextView home_textview_logout=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_home,container,false);

        return view;
    }

    private void initFragment(View view){

        layout_shool_date=(LinearLayout)view.findViewById(R.id.layout_shool_date);
        layout_shool_date.setOnClickListener(new MyClickListener());
        layout_more=(LinearLayout)view.findViewById(R.id.layout_more);
        layout_more.setOnClickListener(new MyClickListener());
        layout_update= (LinearLayout) view.findViewById(R.id.layout_update);
        layout_update.setOnClickListener(new MyClickListener());
        layout_exam= (LinearLayout) view.findViewById(R.id.layout_exam);
        home_imageview_logout= (ImageView) view.findViewById(R.id.home_imageview_logout);
        home_textview_logout= (TextView) view.findViewById(R.id.home_textview_logout);
        layout_dic= (LinearLayout) view.findViewById(R.id.layout_dic);
        layout_second_simple= (LinearLayout) view.findViewById(R.id.layout_second_simple);
        layout_intranet= (LinearLayout) view.findViewById(R.id.layout_intranet);
        layout_logout= (LinearLayout) view.findViewById(R.id.layout_logout);
        layout_logout.setOnClickListener(new MyClickListener());
        layout_second_simple.setOnClickListener(new MyClickListener());
        layout_dic.setOnClickListener(new MyClickListener());
        layout_intranet.setOnClickListener(new MyClickListener());
        layout_exam.setOnClickListener(new MyClickListener());

        if (MainActivity.isLogin()){
            home_imageview_logout.setBackgroundResource(R.drawable.exit);
            home_textview_logout.setText("退出当前账号");
        }else {
            home_imageview_logout.setBackgroundResource(R.drawable.login);
            home_textview_logout.setText("登录");
        }

    }

    @Override
    public void onResume() {
        initFragment(view);
        super.onResume();
    }

    class mySecondScoreThread extends Thread{
        Message message=new Message();
        Bundle bundle=new Bundle();

        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.innovateUrl,"get");
                httpHelp.setHeader("Cookie","JSESSIONID="+MainActivity.getJsessionId());
                httpHelp.setHeader("Referer","http://202.192.240.29/xsjxjhxx!xsjxjhList.action?lx=01");
                httpHelp.setHeader("Accept-Encoding","gzip, deflate");

                HttpResponse httpResponse = httpHelp.getRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = parseGzip(entity);
                    sendSecondScore(response);

                }
            } catch (Exception e) {
                message.what=1007;
                message.setData(bundle);
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }

        /**
         * 解Gzip压缩
         *
         * @throws IOException
         * @throws IllegalStateException
         */
        private String parseGzip(HttpEntity entity) throws Exception {
            InputStream in = entity.getContent();
            GZIPInputStream gzipInputStream = new GZIPInputStream(in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    gzipInputStream, HTTP.UTF_8));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine())!= null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }

        private void sendSecondScore(String string){
            Pattern pattern = Pattern.compile("创新学分：(-?\\d+)(\\.\\d+)?");
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()==false){
                message.what=1006;
                bundle.putString("retcode","0");
            }else {
                String ret= matcher.group();
                message.what=1008;
                bundle.putString("retcode",ret);
            }
            message.setData(bundle);
            handle.sendMessage(message);
        }

    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1006:{
                    MainActivity.setJessionId("123456789");
                    MainActivity.setLogin(false);
                    Toast.makeText(mcontext,"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(mcontext,LoginActivity.class);
                    startActivity(intent);
                    break;
                }
                case 1007:{
                    Toast.makeText(mcontext,R.string.offline,Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1008:{
                    Bundle bundle= msg.getData();
                    String string = bundle.getString("retcode");
                    Toast.makeText(mcontext, string,Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_update:{
                    testUpdate update=new testUpdate(mcontext,getResources().getString(R.string.update),true);
                    break;
                }
                case R.id.layout_dic:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,DictionaryActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.layout_intranet:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","intranet");
                    startActivity(intent);
                    break;
                }
                case R.id.layout_more:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","more");
                    startActivity(intent);
                    break;
                }
                case R.id.layout_exam:{
                    if (MainActivity.isLogin()){
                        Intent intent=new Intent();
                        intent.setClass(mcontext,OtherActivity.class);
                        intent.putExtra("msg","exam");
                        startActivity(intent);
                    }else {
                        Toast.makeText(mcontext,"请先登录",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(mcontext,LoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
                case R.id.layout_second_simple:{
                    if (MainActivity.isLogin()){
                        mySecondScoreThread secondScoreThread=new mySecondScoreThread();
                        secondScoreThread.start();
                    }else {
                        Toast.makeText(mcontext,"请先登录",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(mcontext,LoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
                case R.id.layout_logout:{
                    if (MainActivity.isLogin()){
                        MainActivity.setJessionId("123456789");
                        MainActivity.setLogin(false);

                        home_imageview_logout.setBackgroundResource(R.drawable.login);
                        home_textview_logout.setText("登录");

                        Toast.makeText(mcontext,"已退出",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent=new Intent();
                        intent.setClass(mcontext,LoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
                case R.id.layout_shool_date:{
                    Intent intent=new Intent();
                    intent.setClass(mcontext,OtherActivity.class);
                    intent.putExtra("msg","webView");
                    intent.putExtra("url",UrlUtil.schoolDateUrl);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

}
