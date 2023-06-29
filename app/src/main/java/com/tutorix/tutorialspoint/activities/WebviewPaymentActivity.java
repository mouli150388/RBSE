package com.tutorix.tutorialspoint.activities;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.utility.CustomDialog;

public class WebviewPaymentActivity extends AppCompatActivity {

    WebView webview;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        url=getIntent().getStringExtra("url");
        //Log.v("URL","URL "+url);
        webview=findViewById(R.id.webview);
        webview.getSettings().setCacheMode(LOAD_NO_CACHE);
        webview.getSettings().setJavaScriptEnabled(true);
        //webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebChromeClient(new WebChromeClient(){
        });
        CustomDialog.showDialog(WebviewPaymentActivity.this,true);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

               // Log.d("My Webview","My Webview "+ url);
                try {
                    if(url.contains("origin.tutorix.com")) {
                        CustomDialog.closeDialog();
                        CustomDialog.showDialog(WebviewPaymentActivity.this, true);
                    }
                }catch (Exception e)
                {

                }
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {

                try {
                    CustomDialog.closeDialog();
                }catch (Exception e)
                {

                }
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Here put your code
                //Log.d("My Webview","My Webview "+ url);
                if(url.contains("com.tutorix.payment"))
                {
                    Intent in=new Intent(WebviewPaymentActivity.this, PaymentExecutionActivity.class);
                    in.setData(Uri.parse(url));
                    startActivity(in);
                    finish();
                    return true;
                }
                // return true; //Indicates WebView to NOT load the url;
                return false; //Allow WebView to load url
            }
        });
        webview.loadUrl(url);

    }
}
