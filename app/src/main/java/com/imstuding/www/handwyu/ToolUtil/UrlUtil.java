package com.imstuding.www.handwyu.ToolUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yangkui on 2018/4/9.
 */

public class UrlUtil {

    private Context mcontext;
    private SharedPreferences sharedPreferences;
    //操作
    public static final String operateUrl="http://www.imstuding.com/notice/operate.html";
    //通知
    public static final String noticeUserUrl="http://www.imstuding.com/notice/notice.html";
    //空教室查询
    public static final String emptyClassUrl="http://202.192.240.54/classroom/Default.aspx";
    //统计人数
    public static final String registerUrl="http://www.imstuding.com/app/CountPeople.php";
    //统计打开次数
    public static final String countOpenUrl="http://www.imstuding.com/app/hobby.php";
    //导入课程表
    public static final String  tableUrl ="http://202.192.240.29/xsgrkbcx!getDataList.action";
    //设置周次
    public static final String  setZcUrl ="http://202.192.240.29/xsgrkbcx!xsgrkbMain.action";
    //查询成绩
    public static final String scoreUrl ="http://202.192.240.29/xskccjxx!getDataList.action";
    //验证码
    public static final String verifyUrl ="http://202.192.240.29/yzm?d=1515824347343";
    //刷新
    public static final String refreshScoreInfUrl ="http://202.192.240.29/xxzyxx!reGet.action";
    //登录
    public static final String loginUrl ="http://202.192.240.29/new/login" ;
    //获得学习情况
    public static final String getScoreInfUrl="http://202.192.240.29/xxzyxx!xxzyList.action";
    //图书查找
    public static String libraryUrl ;
    //公交查询
    public static String busUrl ;
    //校历
    public static String schoolDateUrl ;
    //点赞
    public static final String likeUrl="http://www.imstuding.com/app/Like.php";
    //反馈bug
    public static final String  bugUrl ="http://www.imstuding.com/app/ReportBug.php";
    //义工信息
    public static final String voluteerInfUrl ="http://113.107.136.252/Mobile/Service/userinfo.do?sessionStr=";
    //历史服务
    public static final String volunteerHistoryUrl ="http://113.107.136.252/Mobile/Member/activityList.do?type=3&sessionStr=";
    //义工时
    public static final String volunteerTimeUrl ="http://113.107.136.252/Mobile/Member/serviceTotal.do?sessionStr=";
    //下载义工app
    public static final String volunteerAppUrl ="http://jmva.jiangmen.gov.cn/Mobile/download.do";
    //旧系统
    public static final String secondSystemUrl ="http://jwc.wyu.edu.cn/student/";
    //获得cookie
    public static final String secondCookieUrl ="http://jwc.wyu.edu.cn/student/images/logon_text.gif";
    //登录旧系统
    public static final String secondLoginUrl="http://jwc.wyu.edu.cn/student/logon.asp";
    //第二课堂学分
    public static final String secondScoreUrl="http://jwc.wyu.edu.cn/student/f4_myscore11.asp";
    //测试更新
    public static final String updateUrl  ="http://www.imstuding.com/ad/isUpdate.json";
    //有道api
    public static String youDaoUrl ;
    //创新学分
    public static final String innovateUrl ="http://202.192.240.29/xsjxjhxx!xsxxjhMain.action?jxjhdm=J08022015&jhlxdm=01&jhfxdm=&_=1522066510481";
    //考试安排
    public static final String examUrl="http://202.192.240.29/xsksap!getDataList.action";
    //义工登录
    public static final String voluteerLoginUrl="http://113.107.136.252/Mobile/Service/login.do";

    public UrlUtil(Context context){
        mcontext=context;
        sharedPreferences=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initUrl();
    }

    private void initUrl(){
        libraryUrl=sharedPreferences.getString("libraryUrl","http://agentdockingopac.featurelib.libsou.com/showhome/search/showSearch?schoolId=545&xc=6");
        busUrl=sharedPreferences.getString("busUrl","http://www.jmbus.com.cn:2222/cxxl.aspx?from=singlemessage");
        schoolDateUrl=sharedPreferences.getString("schoolDateUrl","http://www.wyu.edu.cn/jwc/xxcx/xxxl.htm");
        youDaoUrl=sharedPreferences.getString("youDaoUrl","http://fanyi.youdao.com/openapi.do?keyfrom=sasfasdfasf&key=1177596287&type=data&doctype=json&version=1.1&q=");
    }

    public void setUpdateUrl(String libraryUrl,String busUrl,String schoolDateUrl,String youDaoUrl){
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("libraryUrl",libraryUrl);
        editor.putString("busUrl",busUrl);
        editor.putString("schoolDateUrl",schoolDateUrl);
        editor.putString("youDaoUrl",youDaoUrl);

        editor.commit();
    }


}
