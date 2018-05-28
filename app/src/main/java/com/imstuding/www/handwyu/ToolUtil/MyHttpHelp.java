package com.imstuding.www.handwyu.ToolUtil;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import java.util.List;

/**
 * Created by yangkui on 2018/3/21.
 */

public class MyHttpHelp {
    private HttpClient httpClient =null;
    private HttpPost httpPost = null;
    private HttpGet httpGet=null;
    boolean flag=false;
    public MyHttpHelp(String url,String mode){
        httpClient = new DefaultHttpClient();
        if (mode=="post"){
            flag=true;
            httpPost = new HttpPost(url);
            //默认的header
            httpPost.setHeader("Origin","http://202.192.240.29");
            httpPost.setHeader("host","202.192.240.29");
            httpPost.setHeader("Connection","keep-alive");
            httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        }else if (mode=="get"){
            flag=false;
            httpGet=new HttpGet(url);
            httpGet.setHeader("Accept","*/*");
            httpGet.setHeader("Connection","keep-alive");
            httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        }

    }

    //发送post请求
    public HttpResponse postRequire(List<NameValuePair> params){
        HttpResponse httpResponse=null;
        try {
            //不为空再设置
            if (params!=null)
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            httpResponse= httpClient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpResponse;
    }

    public HttpResponse getRequire(List<NameValuePair> params){

        HttpResponse httpResponse=null;
        try {
            //不为空再设置
            if (params!=null)
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            httpResponse= httpClient.execute(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpResponse;
    }

    public void setHeader(String name,String value){
        if (flag==true){
            httpPost.setHeader(name,value);
        }else {
            httpGet.setHeader(name,value);
        }

    }

}
