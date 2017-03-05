package com.ateam.funshoppers.Main_navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ateam.funshoppers.Adapters.ImageAdapter;
import com.ateam.funshoppers.R;

import java.util.ArrayList;

public class PhotoDetail extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<String> photos;
    int width;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        gridView=(GridView)findViewById(R.id.gridview);
        WindowManager wm=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        Point size=new Point();
        getSupportActionBar().setTitle("Photos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        display.getSize(size);
        width=(int)(size.x/2.5);
        ArrayList<String> array=new ArrayList<>();
        gridView.setColumnWidth(width);
        ImageAdapter ia=new ImageAdapter(PhotoDetail.this,array,width);
        gridView.setAdapter(ia);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(PhotoDetail.this,SlideshowDialogFragment.class);
                intent.putExtra("images",photos);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });



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
    @Override
    protected void onStart() {
        super.onStart();
        photos=new ArrayList<>();
        Intent intent=getIntent();
        if(intent!=null && intent.hasExtra("photos")){
            photos=intent.getStringArrayListExtra("photos");
        }
        ImageAdapter adapter=new ImageAdapter(this,photos,width);
        gridView.setColumnWidth(width);
        gridView.setAdapter(adapter);
    }
}
