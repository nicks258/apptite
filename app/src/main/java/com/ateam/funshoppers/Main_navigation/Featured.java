package com.ateam.funshoppers.Main_navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ateam.funshoppers.R;


public class Featured extends AppCompatActivity {
    private WebView wv1;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        if(i!=null){
            if(i.hasExtra("url"))
        {
            url=i.getStringExtra("url");
        }
        if(i.hasExtra("title"))
        {
            getSupportActionBar().setTitle(i.getStringExtra("title"));
        }
        }
        wv1=(WebView)findViewById(R.id.webview);
        startWebView(url);
      /*  wv1.setWebViewClient(new MyBrowser());
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(url);*/

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

  /*  private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.clearHistory();
            view.clearCache(true);
        }
    }*/
  private void startWebView(String url) {

      //Create new webview Client to show progress dialog
      //When opening a url or click on link

      wv1.setWebViewClient(new WebViewClient() {
          ProgressDialog progressDialog;

          //If you will not use this method url links are opeen in new brower not in webview
          @Override
          public boolean shouldOverrideUrlLoading(WebView view, String url) {
              view.loadUrl(url);
              return true;
          }

          //Show loader on url load

          @Override
          public void onLoadResource (WebView view, String url) {
              if (progressDialog == null) {
                  // in standard case YourActivity.this
                  progressDialog = new ProgressDialog(Featured.this);
                  progressDialog.setMessage("Loading...");
                  progressDialog.show();
              }
          }

          @Override
          public void onPageFinished(WebView view, String url) {
              try{
                  view.clearHistory();
                  view.clearCache(true);
                  if (progressDialog.isShowing()) {
                      progressDialog.dismiss();
                      progressDialog = null;

                  }
              }catch(Exception exception){
                  view.clearHistory();
                  view.clearCache(true);
                  exception.printStackTrace();
              }
          }
          @Override
          public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
              Toast.makeText(Featured.this, "Error:" + description, Toast.LENGTH_SHORT).show();

          }
      });



      // Javascript inabled on webview
      wv1.getSettings().setJavaScriptEnabled(true);

      // Other webview options

        wv1.getSettings().setLoadWithOverviewMode(true);
        wv1.getSettings().setUseWideViewPort(true);
        wv1.setScrollbarFadingEnabled(false);
        wv1.getSettings().setBuiltInZoomControls(true);
      wv1.getSettings().setLoadsImagesAutomatically(true);
      wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null);
         */

      //Load url in webview
      wv1.loadUrl(url);


  }

    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(wv1.canGoBack()) {
            wv1.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

}

