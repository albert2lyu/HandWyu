package com.imstuding.www.handwyu.AddCourse;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;
import com.imstuding.www.handwyu.WebViewDlg.LoginActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import static com.imstuding.www.handwyu.MainUi.TableFragment.getWeekOfDate;

public class AddCourseActivity extends AppCompatActivity {

    private String tableUrl= UrlUtil.tableUrl;
    private Spinner spinner=null;
    private Button btn_add_course=null;
    private MyLoadDlg myLoadDlg=null;
    private List<String> dataTermList;
    private ArrayAdapter<String> spinnerTermAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        initActivity();
    }

    private void initActivity(){
        spinner= (Spinner) findViewById(R.id.course_spinner_date);
        btn_add_course= (Button) findViewById(R.id.btn_add_course);
        btn_add_course.setOnClickListener(new MyClickListener());

        //////////////创建学期Spinner数据
        fillTermDataList();
        spinnerTermAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dataTermList);
        spinnerTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerTermAdapter);
    }

    public void fillTermDataList() {
        dataTermList = new ArrayList<>();
        String year;
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int month=d.getMonth()+1;
        year=sdf.format(d);

        if (month>=2&&month<=7){
            int cy=Integer.parseInt(year);
            int by=cy-1;
            for (int i=0;i<4;i++){
                dataTermList.add( by+"-"+cy+"-2");
                dataTermList.add( by+"-"+cy+"-1");
                cy--;
                by--;
            }
        }else {
            if (month>=8){
                int cy=Integer.parseInt(year);
                int by=cy-1;
                dataTermList.add( by+"-"+(cy+1)+"-1");
                for (int i=0;i<4;i++){
                    dataTermList.add( by+"-"+cy+"-2");
                    dataTermList.add( by+"-"+cy+"-1");
                    cy--;
                    by--;
                }
            }else {
                int cy=Integer.parseInt(year);
                int by=cy-1;
                dataTermList.add( by+"-"+(cy)+"-1");
                for (int i=0;i<4;i++){
                    cy--;
                    by--;
                    dataTermList.add( by+"-"+cy+"-2");
                    dataTermList.add( by+"-"+cy+"-1");
                }
            }

        }


    }

    class mySetZcThread extends Thread{

        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.setZcUrl,"get");
                httpHelp.setHeader("Cookie","JSESSIONID="+ MainActivity.getJsessionId());
                HttpResponse httpResponse = httpHelp.getRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    getZcFromNet(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void getZcFromNet(String response){
            Pattern pattern = Pattern.compile("&zc=\\d{1,}&");
            Matcher matcher = pattern.matcher(response);
            matcher.find();
            String ret= matcher.group();
            String string=ret.substring(4,ret.length()-1);

            Message message=new Message();
            Bundle bundle=new Bundle();
            try{
                Integer.parseInt(string);
            }catch (Exception e){
                string=1+"";//如果错误就设置当前周为第一周
                e.printStackTrace();
            }
            bundle.putString("zc",string);
            message.setData(bundle);
            message.what=1014;
            handle.sendMessage(message);
        }

    }

    class myTableThread extends Thread{
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(tableUrl,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+MainActivity.getJsessionId());
                httpHelp.setHeader("Accept-Encoding","gzip, deflate");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String xnxqdm=spinner.getSelectedItem().toString();
                xnxqdm=xnxqdm.replaceAll("\\-\\w+\\-","0");
                params.add(new BasicNameValuePair("zc", ""));
                params.add(new BasicNameValuePair("xnxqdm", xnxqdm));
                params.add(new BasicNameValuePair("page", "1"));
                params.add(new BasicNameValuePair("rows", "2000"));
                params.add(new BasicNameValuePair("sort", "kxh"));
                params.add(new BasicNameValuePair("order", "asc"));
                HttpResponse httpResponse = httpHelp.postRequire(params);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = parseGzip(entity);
                    parseJSONWithJSONObject(response);
                }
            } catch (Exception e) {
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

        private void parseJSONWithJSONObject(String jsonData) {
            DatabaseHelper dbhelp=new DatabaseHelper(getApplicationContext(),"course.db",null,1);
            SQLiteDatabase db=dbhelp.getWritableDatabase();
            String xnxqdm=spinner.getSelectedItem().toString();
            xnxqdm=xnxqdm.replaceAll("\\-\\w+\\-","0");
            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONArray retArray=jsonObject.getJSONArray("rows");
                    for (int j = 0; j < retArray.length(); j++) {
                        JSONObject retobject =retArray.getJSONObject(j);
                        String jxcdmc= retobject.getString("jxcdmc");
                        String teaxms= retobject.getString("teaxms");
                        String xq= retobject.getString("xq");
                        String jcdm= retobject.getString("jcdm");
                        String kcmc= retobject.getString("kcmc");
                        String zc= retobject.getString("zc");
                        try{
                            db.execSQL("insert into course values(?,?,?,?,?,?,?)",new String[]{jxcdmc,teaxms,xq,jcdm,kcmc,zc,xnxqdm});
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
                //把数据发送出去
                db.close();
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putInt("retcode",0);
                message.setData(bundle);
                message.what=1005;
                handle.sendMessage(message);
            } catch (Exception e) {
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putInt("retcode",0);
                message.setData(bundle);
                message.what=1006;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }
    }

    public void Removecourse(String xnxqdm){
        DatabaseHelper dbhelp=new DatabaseHelper(getApplicationContext(),"course.db",null,1);
        SQLiteDatabase db=dbhelp.getWritableDatabase();
        try{
            db.execSQL("delete from course where year = ?",new String[]{xnxqdm});
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_add_course:{
                    myLoadDlg=new MyLoadDlg(AddCourseActivity.this);
                    myLoadDlg.show();

                    String xnxqdm=spinner.getSelectedItem().toString();
                    xnxqdm=xnxqdm.replaceAll("\\-\\w+\\-","0");
                    Removecourse(xnxqdm);

                    myTableThread myTableThread=new myTableThread();
                    myTableThread.start();
                    break;
                }
            }
        }

    }


    public void setCurrentZc(String zc){
        DatabaseHelper dbhelp=new DatabaseHelper(getApplicationContext(),"course.db",null,1);
        SQLiteDatabase db=dbhelp.getWritableDatabase();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String rq=sdf.format(d);
        String xq=getWeekOfDate(d);
        db.execSQL("insert into week values(?,?,?)",new String[]{xq,rq,zc});
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1005: {//导入课程表
                    mySetZcThread setZcThread=new mySetZcThread();
                    setZcThread.start();
                    break;
                }
                case 1006: {//没有登录
                    myLoadDlg.dismiss();
                    Toast.makeText(AddCourseActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(AddCourseActivity.this,LoginActivity.class);
                    startActivity(intent);
                    break;
                }
                case 1014:{//自动设置当前周
                    Bundle bundle=msg.getData();
                    String zc=bundle.getString("zc");
                    setCurrentZc(zc);
                    myLoadDlg.dismiss();
                    Toast.makeText(AddCourseActivity.this,"导入成功，并且自动设置了第"+zc+"周为当前周",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

    };
}
