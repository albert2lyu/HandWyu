package com.imstuding.www.handwyu.Dictionaty;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.imstuding.www.handwyu.R;

public class DictionaryActivity extends AppCompatActivity {

    private Searchfragment searchfragment=null;
    private Strangefragment strangefragment=null;
    private Historyfragment historyfragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        initActivity();
        setFragment(searchfragment);

    }

    public void initActivity(){
        searchfragment=new Searchfragment();
        strangefragment=new Strangefragment();
        historyfragment=new Historyfragment();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.dic_navigation);

        ColorStateList csl=getResources().getColorStateList(R.color.navi_text);
        navigation.setItemIconTintList( csl);
        navigation.setItemTextColor(csl);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dic_navigation_search:{
                        setFragment(searchfragment);
                        return true;
                    }
                    case R.id.dic_navigation_book:{
                        setFragment(strangefragment);
                        return true;
                    }
                    case R.id.dic_navigation_history: {
                        setFragment(historyfragment);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.dic_framelayout,fragment);
        transaction.commit();
    }
}
