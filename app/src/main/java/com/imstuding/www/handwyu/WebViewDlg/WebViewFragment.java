package com.imstuding.www.handwyu.WebViewDlg;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangkui on 2018/4/1.
 */

public class WebViewFragment extends Fragment {

    private Context mcontext;
    private View view;
    private WebView webView;
    //private MyLoadDlg myLoadDlg;
    private String url;
    private ProgressBar pg1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_webview,container,false);
        try{
            url=getArguments().getString("url");
        }catch (Exception e){

        }
        setHasOptionsMenu(true);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        pg1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        //myLoadDlg=new MyLoadDlg(mcontext);
        //myLoadDlg.show(false);
        webView= (WebView) view.findViewById(R.id.schooldate_webview);

        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        //Map<String,String> extraHeaders = new HashMap<String, String>();
        //extraHeaders.put("X-Requested-With", "com.jmyg");
        webView.loadUrl(url);
        webView.getSettings().setAppCacheEnabled(false);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SubMenu subMenu=menu.addSubMenu("");
        inflater.inflate(R.menu.web_menu,subMenu);
        MenuItem item=subMenu.getItem();
        item.setIcon(R.drawable.menu);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        super.onCreateOptionsMenu(menu, inflater);
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
