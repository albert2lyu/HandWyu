package com.imstuding.www.handwyu.WebViewDlg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
   // private MyLoadDlg myLoadDlg;
    private String url;
    private Button webback;
    private Button webgo;
    private Button webclose;
    private ProgressBar pg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        try{
            url=getIntent().getStringExtra("url");
        }catch (Exception e){
            url="http://www.wyu.edu.cn/";
            Toast.makeText(WebViewActivity.this,"当你看到这个网页的时候，说明app出错了，请去提bug，谢谢",Toast.LENGTH_SHORT).show();
        }
        initActivity();
    }

    public void initActivity(){
        pg1 = (ProgressBar)findViewById(R.id.progressBar1);
        webback= (Button)findViewById(R.id.webback);
        webgo= (Button)findViewById(R.id.webgo);
        webclose= (Button)findViewById(R.id.webclose);

        webclose.setOnClickListener(new MyClickListener());
        webback.setOnClickListener(new MyClickListener());
        webgo.setOnClickListener(new MyClickListener());

       // myLoadDlg=new MyLoadDlg(this);
       //myLoadDlg.show(false);
        webView= (WebView) findViewById(R.id.schooldate_webview);

        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                if(newProgress==100){
                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
                   // myLoadDlg.dismiss();
                }
                else{
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                }

            }
        });

        webView.loadUrl(url);

    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.webback:{
                    if (webView.canGoBack()){
                        webView.goBack();
                    }else {
                        Toast.makeText(WebViewActivity.this,"不能再后退了",Toast.LENGTH_SHORT).show();
                    }

                    break;
                }
                case R.id.webgo:{
                    if (webView.canGoForward()){
                        webView.goForward();
                    }else {
                        Toast.makeText(WebViewActivity.this,"不能再前进了",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case R.id.webclose:{
                    webView.clearCache(true);
                    webView.clearFormData();
                    webView.clearHistory();
                    finish();
                    break;
                }
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            }else {
                return super.onKeyDown(keyCode, event);
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.clearCache(true);
            webView.clearFormData();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        SubMenu subMenu=menu.addSubMenu("");
        inflater.inflate(R.menu.web_menu,subMenu);
        MenuItem item=subMenu.getItem();
        item.setIcon(R.drawable.menu);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.web_menu_update:{
                webView.reload();
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
