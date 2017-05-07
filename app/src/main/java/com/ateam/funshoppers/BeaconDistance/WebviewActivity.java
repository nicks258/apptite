package com.ateam.funshoppers.BeaconDistance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ateam.funshoppers.R;
import com.orhanobut.logger.Logger;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebviewActivity extends Activity {

    private CoordinatorLayout coordinatorLayout;

    private WebView mWebView = null;

    private LinearLayout mlLayoutRequestError = null;
    private Handler mhErrorLayoutHide = null;

    private boolean mbErrorOccured = false;
    private boolean mbReloadPressed = false;
    String urll = "http://69b689ec.ngrok.io/beacon/pages/locate.php?url=";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String s = getIntent().getStringExtra("url");
        Log.i("InteUrl", s);
        urll = urll + s;
//        String s2 = ".php";
        urll = urll ;
        Log.i("UlrResourcefinal",urll);
        Logger.i("UlrResourcefinal"+ urll);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mlLayoutRequestError = (LinearLayout) findViewById(R.id.lLayoutRequestError);
        mhErrorLayoutHide = getErrorLayoutHideHandler();
        mWebView = (WebView) findViewById(R.id.webviewMain);
        mWebView.setWebViewClient(new MyWebViewClient());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(getChromeClient());
        mWebView.clearCache(true);
        mWebView.loadUrl(urll);
        mWebView.reload();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        mWebView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.canGoBack();











        //caching
        mWebView.getSettings().setAppCacheMaxSize( 20 * 1024 * 1024 * 1024 ); // 5MB
        mWebView.getSettings().setAppCachePath( this.getApplicationContext().getCacheDir().getAbsolutePath() );
        mWebView.getSettings().setAllowFileAccess( true );
        mWebView.getSettings().setAppCacheEnabled( true );
        mWebView.getSettings().setJavaScriptEnabled( true );
        mWebView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT ); // load online by default

        if ( !isNetworkAvailable() ) { // loading offline

        /*    No_internet nextFrag= new No_internet();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.as, nextFrag)
                    .addToBackStack(null)
                    .commit();
*/

        }




        mWebView.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    WebView webView = (WebView) v;

                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                            if(webView.canGoBack())
                            {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });

        mWebView.loadUrl(urll);




    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }











    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url != null && url.startsWith("whatsapp://"))
            {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            }else if(url.startsWith("mailto:")){
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                startActivity(i);
            }
            else if(url.contains("geo:")) {
                Uri gmmIntentUri = Uri.parse(url);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(WebviewActivity.this.getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
            progressDialog.show();
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            // progressDialog.show();
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressDialog.dismiss();
            if (mbErrorOccured == false && mbReloadPressed) {
                hideErrorLayout();
                mbReloadPressed = false;
            }

            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!mbErrorOccured) {
                                return;
                            }

                            mbReloadPressed = true;
                            mWebView.reload();
                            mbErrorOccured = false;

                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.isShown();

            snackbar.show();
            mbErrorOccured = true;
            showErrorLayout();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private WebChromeClient getChromeClient() {
        final ProgressDialog progressDialog = new ProgressDialog(WebviewActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        };
    }

    private void showErrorLayout() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!mbErrorOccured) {
                            return;
                        }

                        mbReloadPressed = true;
                        mWebView.reload();
                        mbErrorOccured = false;

                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.isShown();
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        mlLayoutRequestError.setVisibility(View.VISIBLE);
    }

    private void hideErrorLayout() {
        mhErrorLayoutHide.sendEmptyMessageDelayed(10000, 200);
    }

    private Handler getErrorLayoutHideHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mlLayoutRequestError.setVisibility(View.GONE);

                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}