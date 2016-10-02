package com.ateam.funshoppers.Main_navigation;


import android.app.ProgressDialog;
<<<<<<< HEAD
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
=======
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ateam.funshoppers.R;
import com.rey.material.widget.ProgressView;


public class Home_page extends Fragment {
    GPSTracker gps;


    LocalDatabase localDatabase;
    private CoordinatorLayout coordinatorLayout;

    private WebView mWebView = null;

    private LinearLayout mlLayoutRequestError = null;
    private Handler mhErrorLayoutHide = null;

    private boolean mbErrorOccured = false;
    private boolean mbReloadPressed = false;
    String urll = "http://suvojitkar365.esy.es/apptite/index.php?lat=";
    String url;
private ProgressDialog progressDialog;
=======
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ateam.funshoppers.R;

/**
 * Created by Ratan on 7/29/2015.
 */
public class Home_page extends Fragment {
    GPSTracker gps;

    private WebView wv1;
    LocalDatabase localDatabase;

    String urll = "http://suvojitkar365.esy.es/apptite/index.php?lat=";
    String url;

>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);
        localDatabase = new LocalDatabase(getActivity());
<<<<<<< HEAD
      ProgressView pv_circular = (ProgressView)v.findViewById(R.id.p);
        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);
        mlLayoutRequestError = (LinearLayout) v.findViewById(R.id.lLayoutRequestError);
        mhErrorLayoutHide = getErrorLayoutHideHandler();
=======


>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc
        gps = new GPSTracker(getActivity());
        // check if GPS enabled
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            // \n is for new line
            Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            Contact contact = localDatabase.getLoggedInUser();
             url = urll + latitude+"&long="+longitude+"&phonenumber="+contact.username;
            Log.d("Mehra",url);

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
<<<<<<< HEAD
        mWebView = (WebView) v.findViewById(R.id.webviewMain);
        mWebView.setWebViewClient(new MyWebViewClient());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(getChromeClient());
        mWebView.loadUrl(url);
      progressDialog = new ProgressDialog(getActivity());
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
        mWebView.getSettings().setAppCachePath( getActivity().getApplicationContext().getCacheDir().getAbsolutePath() );
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
=======
        wv1=(WebView)v.findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());
        WebSettings webSettings = wv1.getSettings();
        wv1.getSettings().setJavaScriptEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        wv1.getSettings().setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.getSettings().setDomStorageEnabled(true);
        wv1.canGoBack();



        wv1.setOnKeyListener(new View.OnKeyListener()
>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc
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

<<<<<<< HEAD
        mWebView.loadUrl(url);





        return v;


    }



    private boolean isNetworkAvailable() {
        mWebView.getProgress();
        WebSettings webSettings = mWebView.getSettings();

        webSettings.getAllowFileAccess();
//      Log.d("URL",wv1.getUrl());
=======

        //caching
        wv1.getSettings().setAppCacheMaxSize( 20 * 1024 * 1024 * 1024 ); // 5MB
        wv1.getSettings().setAppCachePath( getActivity().getApplicationContext().getCacheDir().getAbsolutePath() );
        wv1.getSettings().setAllowFileAccess( true );
        wv1.getSettings().setAppCacheEnabled( true );
        wv1.getSettings().setJavaScriptEnabled( true );
        wv1.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT ); // load online by default

        if ( !isNetworkAvailable() ) { // loading offline
            wv1.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        }

        wv1.loadUrl(url);
        return v;
    }

    private boolean isNetworkAvailable() {
>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService( getActivity().CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

<<<<<<< HEAD










    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

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
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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



=======
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc


}