package com.imstuding.www.handwyu.MainUi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.imstuding.www.handwyu.ToolUtil.BottomNavigationViewHelper;
import com.imstuding.www.handwyu.R;


public class MainActivity extends AppCompatActivity {

    private TableFragment tableFragment;
    private ScoreFragment scoreFragment;
    private HomeFragment homeFragment=null;
    private Notice_Fragment notice_fragment=null;
    private static Context static_context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivity();

    }
    /*
    * 初始化activity
    * */
    private void initActivity(){
        static_context =getApplicationContext();
        //初始化fragment
        //notice_fragment=new Notice_Fragment();
        //tableFragment=new TableFragment();
        //scoreFragment=new ScoreFragment();
        //homeFragment=new HomeFragment();
        //setFragment(notice_fragment);//设置课程表为第一个显示

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);//取消滑动风格
        ColorStateList csl=getResources().getColorStateList(R.color.navi_text);
        navigation.setItemIconTintList( csl);
        navigation.setItemTextColor(csl);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_notice:{
                        notice_fragment=new Notice_Fragment();
                        setFragment(notice_fragment);
                        return true;
                    }
                    case R.id.navigation_table:{
                        tableFragment=new TableFragment();
                        setFragment(tableFragment);
                        return true;
                    }
                    case R.id.navigation_score:{
                        scoreFragment=new ScoreFragment();
                        setFragment(scoreFragment);
                        return true;
                    }
                    case R.id.navigation_find: {
                        homeFragment=new HomeFragment();
                        setFragment(homeFragment);
                        return true;
                    }
                }
                return false;
            }
        });
        navigation.setSelectedItemId(R.id.navigation_notice);
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.content,fragment);
        transaction.commit();
    }

    public static void setJessionId(String string){
        static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putString("JSESSIONID",string).commit();
    }

    public static String getJsessionId(){
        return static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("JSESSIONID","");
    }

    public static boolean isLogin(){
        return static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("isLogin",false);
    }

    public static void setLogin(boolean flag){
       static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putBoolean("isLogin",flag).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
