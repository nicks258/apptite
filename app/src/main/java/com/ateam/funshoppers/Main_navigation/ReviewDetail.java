package com.ateam.funshoppers.Main_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;


import com.ateam.funshoppers.Adapters.ReviewAdapter;
import com.ateam.funshoppers.R;
import com.ateam.funshoppers.Rest.ApiClient;
import com.ateam.funshoppers.Rest.ApiInterface;
import com.ateam.funshoppers.model.BusinessReviews;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDetail extends AppCompatActivity {
    String business_id="1";

    private ListView reviews;
    private ArrayList<BusinessReviews> review;
    ReviewAdapter reviewAdapter;
    TextView emptyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        reviews = (ListView) findViewById(R.id.reviewall);
        emptyText = (TextView)findViewById(android.R.id.empty);
        reviews.setEmptyView(emptyText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reviews");
        review_list();
        Intent intent = getIntent();
        if (intent != null) {
            if(intent.hasExtra("id")){
                business_id=intent.getStringExtra("id");
            }


        }
        getReviews();


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("business_id",business_id);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        business_id=savedInstanceState.getString("business_id");
    }
    private void review_list() {
        review=new ArrayList<>();
        reviewAdapter=new ReviewAdapter(this,review);
        reviews.setAdapter(reviewAdapter);
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

    public void getReviews(){

        ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);
        Call<List<BusinessReviews>> call3=apiService.getBusinessReviews(business_id);
        call3.enqueue(new Callback<List<BusinessReviews>>() {
            @Override
            public void onResponse(Call<List<BusinessReviews>> call, Response<List<BusinessReviews>> response) {
                if(response.body()!=null){
                    review.clear();
                    for(int i=0;i<response.body().size();i++) {
                        if(!response.body().get(i).getBody().equals(""))
                            review.add(response.body().get(i));
                    }
                    reviewAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<BusinessReviews>> call, Throwable t) {
                Log.e("call 3 failed",t.toString());

            }
        });
    }

    }

