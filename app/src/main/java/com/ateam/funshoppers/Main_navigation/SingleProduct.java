package com.ateam.funshoppers.Main_navigation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ateam.funshoppers.Adapters.ImageAdapter;
import com.ateam.funshoppers.Adapters.ReviewAdapter;
import com.ateam.funshoppers.R;
import com.ateam.funshoppers.Rest.ApiClient;
import com.ateam.funshoppers.Rest.ApiInterface;
import com.ateam.funshoppers.model.BusinessPictures;
import com.ateam.funshoppers.model.BusinessProducts;
import com.ateam.funshoppers.model.BusinessReviews;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ateam.funshoppers.Utility.haveNetworkConnection;

public class SingleProduct extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    public int width,width2;
    TextView ratings;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBar;
    private TextView prodName;
    private TextView cost;
    TextView features;
    private TextView ratingshow,description;
    private String productId="";
    private ImageButton coverphoto;
    private GridView photos;
    private Button photosdetail,reviewdetail,share,buy;
    private ListView reviews;
    Intent intent;
    ArrayList<String> images;
    ImageAdapter iadapter;
    ArrayList<BusinessReviews> reviewList;
    ReviewAdapter reviewAdapter;
    ProgressDialog progressDialog;


    ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);

    public void findviews(){
        toolbar=(Toolbar)findViewById(R.id.tool_bar);
        ratings=(TextView)findViewById(R.id.ratingshow);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        appBar=(AppBarLayout)findViewById(R.id.appbar);
        //included layout
        prodName=(TextView)findViewById(R.id.prod_name);
        cost=(TextView)findViewById(R.id.cost);
        features=(TextView)findViewById(R.id.features);
        ratingshow=(TextView)findViewById(R.id.ratingshow);
        coverphoto=(ImageButton)findViewById(R.id.backdrop);
        coverphoto.setOnClickListener(this);
        description=(TextView)findViewById(R.id.description);
        //initializing grid and listviews
        photos=(GridView)findViewById(R.id.gridview);
        reviews=(ListView)findViewById(R.id.reviews);
        photosdetail=(Button)findViewById(R.id.photosee);
        reviewdetail=(Button)findViewById(R.id.reviewall);
        share=(Button)findViewById(R.id.share);
        buy=(Button)findViewById(R.id.buy);
        photosdetail.setOnClickListener(this);
        share.setOnClickListener(this);
        buy.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);
        WindowManager wm=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        width=(int)(size.x/(3));
        width2=size.x;
        findviews();
        setuptoolbar();
        intent=getIntent();
        photo_grid();
        review_list();
        if(intent!=null && intent.hasExtra("id")){
            productId=intent.getStringExtra("id");

        }






    }

    @Override
    protected void onStart() {
        super.onStart();
        if(haveNetworkConnection(SingleProduct.this)){
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(SingleProduct.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
            excute();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SingleProduct.this, R.style.AppCompatAlertDialogStyle);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("business_id",productId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        productId=savedInstanceState.getString("business_id");
    }

    private void photo_grid() {
        images=new ArrayList<>();
        photos.setColumnWidth(width);
        iadapter=new ImageAdapter(this,images,width);
        photos.setAdapter(iadapter);
        photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(SingleProduct.this, PhotoDetail.class);
                i.putExtra("photos",images);
                startActivity(i);
                return;

            }
        });
    }
    private void review_list() {
        reviewList=new ArrayList<>();
        reviewAdapter=new ReviewAdapter(this,reviewList);
        reviews.setAdapter(reviewAdapter);
    }

    private void excute() {
        Call<BusinessProducts> call=apiService.getProductDetails(productId);
        //for product details
        call.enqueue(new Callback<BusinessProducts>() {
            @Override
            public void onResponse(Call<BusinessProducts> call, Response<BusinessProducts> response) {
                if(response.body()!=null){
                    ArrayList<BusinessProducts.Variants> variantses=new ArrayList<BusinessProducts.Variants>();
                    String rs=getResources().getString(R.string.rs);
                    String costType;
                    prodName.setText(response.body().getName());
                    getSupportActionBar().setTitle(response.body().getName());
                    ratingshow.setText(response.body().getRating()+"/10");
                    description.setText(response.body().getDescription());
                    variantses.add(response.body().getVariants().get(0));
                    costType=variantses.get(0).getPriceTypeOption();
                    if(costType.equals("Fixed")){
                        cost.setText(String.valueOf(rs+variantses.get(0).getPriceStart()));
                    }
                    else {
                        cost.setText(String.valueOf(rs+variantses.get(0).getPriceStart()+" - "+rs+variantses.get(0).getPriceEnd()));
                    }
                    String businessPhoto=response.body().getCoverphotourl();
                    if(images.size()==0){
                        images.add(0,businessPhoto);}
                    else if(!images.get(0).equals(businessPhoto)){
                        images.add(0,businessPhoto);
                    }
                    iadapter.notifyDataSetChanged();

                    Picasso.with(SingleProduct.this).load(response.body().getCoverphotourl()).resize(coverphoto.getWidth(),coverphoto.getHeight()).into(coverphoto);

                    populategrid();
                }
            }

            @Override
            public void onFailure(Call<BusinessProducts> call, Throwable t) {
                Log.e("call failed",t.toString());
                populategrid();
            }
        });

        //for getting the reviews
        Call<List<BusinessReviews>> call2=apiService.getProductReviews(productId);
        call2.enqueue(new Callback<List<BusinessReviews>>() {
            @Override
            public void onResponse(Call<List<BusinessReviews>> call, Response<List<BusinessReviews>> response) {
                if(response.body()!=null){
                    reviewList.clear();
                        reviewList.addAll(response.body());
                    reviewAdapter.notifyDataSetChanged();

                    //FoodsTree.setListViewHeightBasedOnChildren(reviews);

                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;

                }
            }

            @Override
            public void onFailure(Call<List<BusinessReviews>> call, Throwable t) {
                Log.e("call2 failed",t.toString());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;

                }
            }
        });


    }
    public void populategrid(){
        //GETTING PICTURES
        Call<List<BusinessPictures>> call1=apiService.getProductPictures(productId);
        call1.enqueue(new Callback<List<BusinessPictures>>() {
            @Override
            public void onResponse(Call<List<BusinessPictures>> call, Response<List<BusinessPictures>> response) {
                if(response.body()!=null)
                {
                for (int i=0;i<response.body().size();i++){
                    images.add(response.body().get(i).getUrl());
                }
                    iadapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onFailure(Call<List<BusinessPictures>> call, Throwable t) {
                Log.e("call1 failed",t.toString());

            }
        });


    }

    private void setuptoolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Details");

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        //collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        // collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        AppBarLayout.OnOffsetChangedListener mListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(collapsingToolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
                    ratings.setVisibility(View.INVISIBLE);
                } else {
                    ratings.setVisibility(View.VISIBLE);
                }
            }
        };

        appBar.addOnOffsetChangedListener(mListener);



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
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.share:
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                String tosend=images.get(0).replace("-240x","");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT,tosend);
                try {
                   startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this,"Whatsapp have not been installed.",Toast.LENGTH_LONG);
                }
                break;
            case R.id.buy:
                Intent i=new Intent(SingleProduct.this,PinLockActivity.class);
                i.putExtra("count",3);
                startActivity(i);

                break;
            case R.id.backdrop:
                Intent intent1=new Intent(SingleProduct.this, SlideshowDialogFragment.class);
                intent1.putExtra("images",images);
                intent1.putExtra("position",0);
                startActivity(intent1);
                return;
            case R.id.photosee:
                Intent i2=new Intent(this, PhotoDetail.class);
                i2.putExtra("photos",images);
                startActivity(i2);
                return;


        }

    }
}
