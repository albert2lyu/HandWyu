package com.imstuding.www.handwyu.Dictionaty;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

/**
 * Created by yangkui on 2017/12/18.
 */

public class Searchfragment extends Fragment {
    private AutoCompleteTextView dic_input=null;
    private TextView dic_retsult=null;
    private TextView dic_word=null;
    private Button dic_search=null;
    private ArrayAdapter<String> arrayAdapter=null;
    private List<String> list=null;
    private ListView listView=null;
    private SimpleAdapter simpleAdapter=null;
    private Button correct_yes=null;
    private Button correct_cancel=null;
    private EditText correct_word=null;
    private EditText correct_symbol=null;
    private EditText correct_meaning=null;
    private AlertDialog alertDialog=null;
    private LinearLayout search_layout=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search_word,null);
        InitSearchFragment(view);
        dic_search.setOnClickListener(new MyClickListener());
        return view;
    }

    public void InitSearchFragment(View view){
        listView=(ListView)view.findViewById(R.id.list_search_result);
        registerForContextMenu(listView);//注册长按事件的上下文menu
        listView.setOnItemClickListener(new MyItemClickListener());
        search_layout=(LinearLayout)view.findViewById(R.id.Search_layout);
        search_layout.setOnClickListener(new MyClickListener());
        dic_word=(TextView)view.findViewById(R.id.dic_word);
        dic_retsult=(TextView)view.findViewById(R.id.dic_result);
        dic_input=(AutoCompleteTextView)view.findViewById(R.id.dic_input);
        dic_search = (Button) view.findViewById(R.id.dic_search);
        list=new ArrayList<String>();
        dic_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dic_search.callOnClick();
            }
        });

        dic_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    dic_search.callOnClick();
                    return true;
                }
                    return false;
            }
        });

    }

    //button点击监视器
    class MyClickListener implements View.OnClickListener{

        Context context= getActivity().getApplicationContext();
        DBOpenHelper dbOpenHelper=new DBOpenHelper(context);
        SQLiteDatabase db= dbOpenHelper.openDatabase("words.db",R.raw.words);
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.dic_search:{
                    List<Map<String,String>> data=new ArrayList<Map<String,String>>();
                    if (db!=null)
                    {
                        String str="SELECT word,symbol,meaning FROM cet_4 where word like '%"+dic_input.getText().toString()+"%'";
                        String sql= null;
                        try {
                            sql = new String(str.getBytes("GBK"),"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Cursor cursor = db.rawQuery(sql, null);
                        if (cursor.getCount()==0){
                            //这里进行有道的api查询
                            SearchOnInternet test = new SearchOnInternet(dic_input.getText().toString());
                            test.start();
                        }
                        while (cursor.moveToNext()){
                            String word=cursor.getString(0);
                            String symbol=cursor.getString(1);
                            String meaning=cursor.getString(2);
                            if (word.equals(dic_input.getText().toString().toLowerCase())) {
                                dic_word.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                dic_retsult.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                dic_word.setText("单词：" + word + "   音标：" + symbol);
                                dic_retsult.setText("\r\n" + "中文解释：" + meaning);
                                operateHistory(word,symbol,meaning);
                            }
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("word",word);
                            map.put("symbol",symbol);
                            map.put("meaning",meaning);
                            data.add(map);
                        }
                        simpleAdapter=new SimpleAdapter(getActivity(),data,R.layout.list_strange_item,
                                new String[]{"word","symbol","meaning"},new int[]{R.id.item_word,R.id.item_symbol,R.id.item_meaning});
                        listView.setAdapter(simpleAdapter);

                    }
                    break;
                }
                case R.id.correct_cancel:{
                    Toast.makeText(getActivity(),"你点击了取消！",Toast.LENGTH_SHORT);
                    alertDialog.dismiss();
                    break;
                }
                case R.id.correct_yes:{
                    try {
                        db.execSQL("UPDATE cet_4 SET symbol=? ,meaning=? WHERE word=?",
                                new String[]{correct_symbol.getText().toString(), correct_meaning.getText().toString(), correct_word.getText().toString()});
                    }catch (SQLiteConstraintException e){
                        Toast.makeText(getActivity(),"纠正出现错误！",Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                    break;
                }
                case R.id.Search_layout:{
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
                    if (isOpen){
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    break;
                }
                default:
                    break;
            }

        }

    }

    //listview点击的监视器
    class MyItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SimpleAdapter simpleAdapter=(SimpleAdapter)parent.getAdapter();
                    Map<String,String> data= (Map<String,String>)simpleAdapter.getItem(position);
                    String word=data.get("word");
                    String symbol=data.get("symbol");
                    String meaning=data.get("meaning");

                    dic_word.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    dic_retsult.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    dic_word.setText("单词：" + word + "   音标："+symbol);
                    dic_retsult.setText("\r\n" + "中文解释：" + meaning);
                    dic_input.setText(word);
                    operateHistory(word,symbol,meaning);
                    //Toast.makeText(getActivity(),"position="+position+",id="+id+",名字 ="+word,Toast.LENGTH_SHORT).show();
        }

    }

    //context菜单点击的监视器
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //info.id得到listview中选择的条目绑定的id
        String id = String.valueOf(info.id);
        ListView lv=(ListView)info.targetView.getParent();
        SimpleAdapter simpleAdapter=(SimpleAdapter)lv.getAdapter();
        Map<String,String> data= (Map<String,String>)simpleAdapter.getItem(Integer.parseInt(id));
        switch (item.getItemId()) {
            case R.id.menu_addstrange:{
                addToStrange(data.get("word"),data.get("symbol"),data.get("meaning"));
                return true;
            }
            case R.id.menu_correct: {
                correctWord(data.get("word"),data.get("symbol"),data.get("meaning"));
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void addToStrange(String word,String symbol,String meaning){
        Context context= getActivity().getApplicationContext();
        DBOpenHelper dbOpenHelper=new DBOpenHelper(context);
        SQLiteDatabase db= dbOpenHelper.openDatabase("strange.db",R.raw.strange);
        String sql="INSERT INTO stanger_words VALUES(?,?,?)";
        try{
            db.execSQL(sql,new String[]{word,symbol,meaning});
        }catch (SQLiteConstraintException e){
            Toast.makeText(getActivity(),"单词:"+word+" 已经存在于生词本！",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getActivity(),"单词："+word+" 加入生词本成功！",Toast.LENGTH_SHORT).show();
    }

    public void correctWord(String word,String symbol,String meaning){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.correct_word_layout, null);
        builder.setView(view);
        builder.setCancelable(false);

        correct_yes=(Button)view.findViewById(R.id.correct_yes);
        correct_cancel=(Button)view.findViewById(R.id.correct_cancel);
        correct_word=(EditText)view.findViewById(R.id.correct_word);
        correct_symbol=(EditText)view.findViewById(R.id.correct_symbol);
        correct_meaning=(EditText)view.findViewById(R.id.correct_meaning);

        correct_word.setText(word);
        correct_symbol.setText(symbol);
        correct_meaning.setText(meaning);
        correct_cancel.setOnClickListener(new MyClickListener());
        correct_yes.setOnClickListener(new MyClickListener());

        alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.list_search_result) {
            MenuInflater inflater = getActivity().getMenuInflater();
            menu.setHeaderTitle("你喜欢啥子操作？");
            inflater.inflate(R.menu.context_menu, menu);
        }
        setMenuIconEnable(menu,true);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //通过反射来设置菜单图标
    public void setMenuIconEnable(ContextMenu menu,boolean enable){
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void operateHistory(String word,String symbol,String meaning){
        Context context= getActivity().getApplicationContext();
        DBOpenHelper dbOpenHelper=new DBOpenHelper(context);
        SQLiteDatabase db= dbOpenHelper.openDatabase("history.db",R.raw.history);
        String sql="INSERT INTO history_word VALUES(?,?,?)";
        try{
            db.execSQL(sql,new String[]{word,symbol,meaning});
        }catch (SQLiteConstraintException e){
            return;
        }
    }


    class SearchOnInternet extends Thread {
        private String string;
        private String sql;
        public  SearchOnInternet(String strq){
            try {
                string=strq;
                sql = URLEncoder.encode(strq,"utf-8");
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        @Override
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(
                        UrlUtil.youDaoUrl +sql);
                HttpResponse httpResponse = httpClient.execute(httpGet);
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
                String symbol="";
                String meaning="";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    try {
                        JSONObject basic=jsonObject.getJSONObject("basic");
                        meaning = basic.getString("explains");
                        try{
                            symbol=basic.getString("phonetic");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                        meaning = jsonObject.getString("translation");
                        e.printStackTrace();
                    }

                    ChineseAndEnglish chineseAndEnglish=new ChineseAndEnglish();
                    if (chineseAndEnglish.isEnglish(string)){
                        Context context= getActivity().getApplicationContext();
                        DBOpenHelper dbOpenHelper=new DBOpenHelper(context);
                        SQLiteDatabase db= dbOpenHelper.openDatabase("words.db",R.raw.words);
                        String str="INSERT INTO cet_4 VALUES(?,?,?)";
                        db.execSQL(str,new String[]{string,symbol,meaning});
                    }
                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("word",string);
                    bundle.putString("symbol",symbol);
                    bundle.putString("meaning",meaning);
                    message.setData(bundle);
                    handle.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle= msg.getData();
            String word= bundle.getString("word");
            String symbol= bundle.getString("symbol");
            String meaning= bundle.getString("meaning");
            dic_word.setBackgroundColor(Color.parseColor("#FFFFFF"));
            dic_retsult.setBackgroundColor(Color.parseColor("#FFFFFF"));
            dic_word.setText("单词：" + word + "   音标："+symbol);
            dic_retsult.setText("\r\n" + "中文解释：" + meaning);
        }

    };

}