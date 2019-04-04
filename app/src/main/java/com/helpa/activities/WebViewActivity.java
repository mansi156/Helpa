package com.helpa.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.helpa.R;


/**
 * Created by appinventiv on 10/10/17.
 */

public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initializeUI();
        setWebView("http://www.google.com");
    }

    private void initializeUI()
    {
        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }


    private void setWebView(String url)
    {
        try {
            webView.loadUrl(url);
            webView.setWebViewClient(new RealWebViewClient());
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (progress == 100)
                        progressBar.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            //Log.e("Web Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private class RealWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                view.loadUrl(url);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return true;
        }
    }

}
