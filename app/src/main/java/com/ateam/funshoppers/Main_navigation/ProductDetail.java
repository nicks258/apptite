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

import com.ateam.funshoppers.Adapters.ProductAdapter;
import com.ateam.funshoppers.R;
import com.ateam.funshoppers.Recyclerview.GridSpacingItemDecoration;
import com.ateam.funshoppers.Recyclerview.RecyclerItemClickListener;
import com.ateam.funshoppers.Rest.ApiClient;
import com.ateam.funshoppers.Rest.ApiInterface;
import com.ateam.funshoppers.model.BusinessProducts;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ateam.funshoppers.Utility.haveNetworkConnection;

/**
 * Created by Home on 11/30/2016.
 */

public class ProductDetail extends AppCompatActivity {
    TextView type;
    RecyclerView list;
    String id="";
    Intent i;
    boolean business;
    String name="";
    ProgressDialog progressDialog;
    ProductAdapter businessAdapter;
    ArrayList<BusinessProducts> productses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);
        //type=(TextView)findViewById(R.id.type);
        //type.setText("Products");
        list=(RecyclerView)findViewById(R.id.businesslist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productses=new ArrayList<>();
        businessAdapter=new ProductAdapter(this,productses);
            GridLayoutManager mLayoutManager2 = new GridLayoutManager(this, 2);
                list.setLayoutManager(mLayoutManager2);
                list.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
                list.setItemAnimator(new DefaultItemAnimator());
                list.setAdapter(businessAdapter);
                list.addOnItemTouchListener(
                        new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent i=new Intent(ProductDetail.this,SingleProduct.class);
                                i.putExtra("id",productses.get(position).getId());
                                startActivity(i);
                            }
                        })
                );

        i=getIntent();
        if(i!=null){
            if (i.hasExtra("name")){
                getSupportActionBar().setTitle(i.getStringExtra("name"));
            }
            if(i.hasExtra("id"))
        {
            id=i.getStringExtra("id");


    }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(haveNetworkConnection(ProductDetail.this)){
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(ProductDetail.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
            excute();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetail.this, R.style.AppCompatAlertDialogStyle);
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
       Call<List<BusinessProducts>> call=apiService.getBusinessProducts(id);
        call.enqueue(new Callback<List<BusinessProducts>>() {
            @Override
            public void onResponse(Call<List<BusinessProducts>> call, Response<List<BusinessProducts>> response) {
                productses.clear();
                productses.addAll(response.body());
                businessAdapter.notifyDataSetChanged();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;

                }
            }

            @Override
            public void onFailure(Call<List<BusinessProducts>> call, Throwable t) {
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
