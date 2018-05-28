package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imstuding.www.handwyu.AddCourse.AddCourseFragment;
import com.imstuding.www.handwyu.AddCourse.SubCourseFragment;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.VolunteerDlg.VolunteerFragment;
import com.imstuding.www.handwyu.VolunteerDlg.VolunteerLoginFragment;
import com.imstuding.www.handwyu.WebViewDlg.WebViewFragment;

public class OtherActivity extends AppCompatActivity {

    private ExamFragment examFragment=null;
    private SecondClassScoreFragment secondClassScoreFragment=null;
    private WebViewFragment webViewFragment =null;
    private MoreFragment moreFragment=null;
    private HelpFragment helpFragment=null;
    private AboutMeFragment aboutMeFragment=null;
    private BugFragment bugFragment=null;
    private UpdateExplainFragment updateExplainFragment =null;
    private VolunteerFragment volunteerFragment=null;
    private VolunteerLoginFragment volunteerLoginFragment=null;
    private AddCourseFragment addCourseFragment=null;
    private IntranetFragment intranetFragment=null;
    private SubCourseFragment subCourseFragment=null;
    private String msg="exam";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        try {
            Intent intent=getIntent();
            msg=intent.getStringExtra("msg");
        }catch (Exception e){

        }
       // initActivity();
        selectFragment(msg);
    }

    public void selectFragment(String string){
        switch (string){
            case "exam":{
                examFragment=new ExamFragment();
                setFragment(examFragment);
                break;
            }
            case "second":{
                secondClassScoreFragment=new SecondClassScoreFragment();
                setFragment(secondClassScoreFragment);
                break;
            }
            case "webView":{
                Bundle bundle=new Bundle();
                String url=getIntent().getStringExtra("url");
                bundle.putString("url",url);

                webViewFragment =new WebViewFragment();
                webViewFragment.setArguments(bundle);
                setFragment(webViewFragment);
                break;
            }
            case "more":{
                moreFragment=new MoreFragment();
                setFragment(moreFragment);
                break;
            }
            case "help":{
                helpFragment=new HelpFragment();
                setFragment(helpFragment);
                break;
            }
            case "about":{
                aboutMeFragment=new AboutMeFragment();
                setFragment(aboutMeFragment);
                break;
            }
            case "bug":{
                bugFragment=new BugFragment();
                setFragment(bugFragment);
                break;
            }
            case "update_explain":{
                updateExplainFragment =new UpdateExplainFragment();
                setFragment(updateExplainFragment);
                break;
            }
            case "volunteer":{
                volunteerFragment=new VolunteerFragment();
                setFragment(volunteerFragment);
                break;
            }
            case "volunteer_login":{
                volunteerLoginFragment=new VolunteerLoginFragment();
                setFragment(volunteerLoginFragment);
                break;
            }
            case "add_course":{
                addCourseFragment=new AddCourseFragment();
                try {
                    Bundle bundle=new Bundle();
                    int xq=getIntent().getIntExtra("xq",1);
                    int js=getIntent().getIntExtra("js",1);
                    bundle.putInt("xq",xq);
                    bundle.putInt("js",js);
                    addCourseFragment.setArguments(bundle);
                }catch (Exception e){

                }
                setFragment(addCourseFragment);
                break;
            }
            case "intranet":{
                intranetFragment=new IntranetFragment();
                setFragment(intranetFragment);
                break;
            }
            case "subCourse":{
                subCourseFragment=new SubCourseFragment();
                setFragment(subCourseFragment);
                break;
            }
        }
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.other_framelayout,fragment);
        transaction.commit();
    }

}
