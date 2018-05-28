package com.imstuding.www.handwyu.Dictionaty;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imstuding.www.handwyu.R;
/**
 * Created by yangkui on 2017/12/19.
 */

public class Historyfragment extends Fragment {
    private ListView listView=null;
    private TextView showtext_word=null;
    private TextView showtext_symbol=null;
    private TextView showtext_meaning=null;
    private AlertDialog alertDialog=null;
    private SimpleAdapter simpleAdapter=null;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.history_word,null);
        InitHistoryFragment(view);
        return view;
    }

    //初始化历史记录的fragment
    public void InitHistoryFragment(View view){
        listView=(ListView)view.findViewById(R.id.list_history_word);
        registerForContextMenu(listView);//注册长按事件的上下文menu
        setOrUpdateSimpleAdapter();
        listView.setOnItemClickListener(new MyItemClickListener());
    }

    //点击listview响应的事件
    class MyItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SimpleAdapter simpleAdapter=(SimpleAdapter)parent.getAdapter();
            Map<String,String> data= (Map<String,String>)simpleAdapter.getItem(position);
            String word=data.get("word");
            String symbol=data.get("symbol");
            String meaning=data.get("meaning");

            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            LayoutInflater inflater =getActivity().getLayoutInflater();
            View showword = inflater.inflate(R.layout.show_word_layout, null);
            builder.setView(showword);

            showtext_word=(TextView)showword.findViewById(R.id.showtext_word);
            showtext_symbol=(TextView)showword.findViewById(R.id.showtext_symbol);
            showtext_meaning=(TextView)showword.findViewById(R.id.showtext_meaning);

            showtext_word.setText(word);
            showtext_symbol.setText(symbol);
            showtext_meaning.setText(meaning);

            alertDialog=builder.create();
            alertDialog.show();
        }

    }

    //长按listview事件对应的事件，context菜单点击的监视器
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //info.id得到listview中选择的条目绑定的id
        String id = String.valueOf(info.id);
        ListView lv=(ListView)info.targetView.getParent();
        SimpleAdapter simpleAdapter=(SimpleAdapter)lv.getAdapter();
        Map<String,String> data= (Map<String,String>)simpleAdapter.getItem(Integer.parseInt(id));
        switch (item.getItemId()) {
            case R.id.history_menu_remove:{
                removeWord(data.get("word"));
                setOrUpdateSimpleAdapter();
               // Toast.makeText(getActivity(),"（历史）移除"+data.get("word"),Toast.LENGTH_SHORT).show();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void removeWord(String word){
        Context context= getActivity().getApplicationContext();
        DBOpenHelper dbOpenHelper=new DBOpenHelper(context);
        SQLiteDatabase db= dbOpenHelper.openDatabase("history.db",R.raw.history);
        String sql="DELETE FROM history_word WHERE word=?";
        try{
            db.execSQL(sql,new String[]{word});
        }catch (Exception e){
            Toast.makeText(getActivity(),"单词:"+word+" 从历史记录移除失败！",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getActivity(),"单词："+word+" 从历史记录移除成功！",Toast.LENGTH_SHORT).show();
    }

    //更新listview里面的数据
    public void setOrUpdateSimpleAdapter(){
        simpleAdapter=new SimpleAdapter(getActivity(),getData(),R.layout.list_strange_item,
                new String[]{"word","symbol","meaning"},new int[]{R.id.item_word,R.id.item_symbol,R.id.item_meaning});
        listView.setAdapter(simpleAdapter);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.list_history_word) {
            MenuInflater inflater = getActivity().getMenuInflater();
            menu.setHeaderTitle("你喜欢啥子操作？");
            inflater.inflate(R.menu.hisrtory_context_menu, menu);
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

    public List<Map<String,String>> getData(){
        List<Map<String,String>> data=new ArrayList<Map<String,String>>();
        Context context= getActivity().getApplicationContext();
        DBOpenHelper dbOpenHelper=new DBOpenHelper(context);
        SQLiteDatabase db= dbOpenHelper.openDatabase("history.db",R.raw.history);
        if (db!=null)
        {
            String str="SELECT word,symbol,meaning FROM history_word";
            String sql= null;
            try {
                sql = new String(str.getBytes("GBK"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                String word=cursor.getString(0);
                String symbol=cursor.getString(1);
                String meaning=cursor.getString(2);
                Map<String,String> map=new HashMap<String, String>();
                map.put("word",word);
                map.put("symbol",symbol);
                map.put("meaning",meaning);
                data.add(map);
            }
        }
        return data;
    }

}
