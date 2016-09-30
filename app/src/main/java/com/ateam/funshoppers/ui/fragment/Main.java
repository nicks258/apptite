package com.ateam.funshoppers.ui.fragment;


import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ateam.funshoppers.R;

public class Main extends Fragment {


    private WebView wv1;




    private ProgressDialog loading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        Log.d("Sumit","Nicks");
        wv1=(WebView)v.findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());
        WebSettings webSettings = wv1.getSettings();
        wv1.getSettings().setJavaScriptEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        wv1.getSettings().setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.getSettings().setDomStorageEnabled(true);



        String url = "http://suvojitkar365.esy.es/apptite/";
        //caching
        wv1.getSettings().setAppCacheMaxSize( 20 * 1024 * 1024 ); // 5MB
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
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService( getActivity().CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}