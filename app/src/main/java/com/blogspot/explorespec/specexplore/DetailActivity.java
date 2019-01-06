package com.blogspot.explorespec.specexplore;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tuyenmonkey.mkloader.MKLoader;

public class DetailActivity extends AppCompatActivity {
    WebView webView;
    MKLoader loader;
    Toolbar tbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkBlack));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.darkBlack));
        } else {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        tbar = findViewById(R.id.detailTool);
        setSupportActionBar(tbar);
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            tbar.setBackgroundColor(getResources().getColor(R.color.darkBlack));
            tbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
            tbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back_white));
            tbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            tbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
            tbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        loader = findViewById(R.id.progressBar);
        webView = findViewById(R.id.detailView);
        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loader.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                String javaScript = "javascript:(function() {var " +
                        "a= document.getElementsByTagName('header'); " +
                        "a[0].hidden='true';" +
                        "a= document.getElementsByTagName('footer'); " +
                        "a[0].hidden='true';" +
                        "a= document.getElementsByClassName('page_body'); " +
                        "a[0].style.padding='0px';" +
                        "a= document.getElementsByClassName('comments'); " +
                        "a[0].style.padding='0px';})()";
                webView.loadUrl(javaScript);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.setVisibility(View.INVISIBLE);
                Toast.makeText(DetailActivity.this, "Error loading the content", Toast.LENGTH_SHORT).show();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webView.loadUrl(getIntent().getStringExtra("url"));
    }
}
