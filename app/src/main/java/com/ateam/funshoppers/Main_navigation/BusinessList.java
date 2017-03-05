package com.ateam.funshoppers.Main_navigation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ateam.funshoppers.Adapters.BusinessAdapter;
import com.ateam.funshoppers.R;
import com.ateam.funshoppers.Recyclerview.GridSpacingItemDecoration;
import com.ateam.funshoppers.Recyclerview.RecyclerItemClickListener;
import com.ateam.funshoppers.Rest.ApiClient;
import com.ateam.funshoppers.Rest.ApiInterface;
import com.ateam.funshoppers.model.Businesses;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ateam.funshoppers.Utility.haveNetworkConnection;

public class BusinessList extends AppCompatActivity {
TextView type;
    RecyclerView list;
    int types;
    String typo;
    Intent i;
    boolean business;
    BusinessAdapter businessAdapter;
    ArrayList<Businesses> businesses;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);
        type=(TextView)findViewById(R.id.type);
        list=(RecyclerView)findViewById(R.id.businesslist);
        businesses=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        i=getIntent();
        if(i!=null && i.hasExtra("type")){
                businessAdapter=new BusinessAdapter(this,businesses);
                typo=i.getStringExtra("type");
                GridLayoutManager mLayoutManager2 = new GridLayoutManager(this, 2);
                list.setLayoutManager(mLayoutManager2);
                list.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
                list.setItemAnimator(new DefaultItemAnimator());
                list.setAdapter(businessAdapter);
                list.addOnItemTouchListener(
                        new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent i=new Intent(BusinessList.this,SingleBusiness.class);
                                i.putExtra("id",businesses.get(position).getId());
                                i.putExtra("name",businesses.get(position).getName());
                                startActivity(i);
                            }
                        })
                );

        }
        getSupportActionBar().setTitle(typo);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(haveNetworkConnection(BusinessList.this)){
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(BusinessList.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
            excute();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(BusinessList.this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("No Internet");
            builder.setMessage("Click on Setting to connect");
            builder.setPositiveButton("OK", null);

            builder.setNegativeButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            builder.show();
        }
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



    public void excute(){
        ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);
        Call<List<Businesses>> call=apiService.getBusinesses();
        call.enqueue(new Callback<List<Businesses>>() {
            @Override
            public void onResponse(Call<List<Businesses>> call, Response<List<Businesses>> response) {
                if(typo.equals("fashion")){
                    for(int i=0;i<response.body().size();i++){
                        if(response.body().get(i).getCategories().contains(1)){
                            businesses.add(response.body().get(i));
                        }
                    }
                }
                else if(typo.equals("food")){
                    for(int i=0;i<response.body().size();i++){
                        if(response.body().get(i).getCategories().contains(2)){
                            businesses.add(response.body().get(i));
                        }
                    }
                }
                else if(typo.equals("lifestyle")){
                    for(int i=0;i<response.body().size();i++){
                        if(response.body().get(i).getCategories().contains(3)){
                            businesses.add(response.body().get(i));
                        }
                    }
                }
                businessAdapter.notifyDataSetChanged();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;

                }
            }

            @Override
            public void onFailure(Call<List<Businesses>> call, Throwable t) {

                Log.e("call failed",t.toString());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;

                }
            }
        });
    }
    public  int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
