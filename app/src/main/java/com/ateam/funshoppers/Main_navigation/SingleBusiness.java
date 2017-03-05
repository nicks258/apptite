package com.ateam.funshoppers.Main_navigation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ateam.funshoppers.Adapters.ImageAdapter;
import com.ateam.funshoppers.Adapters.ProductAdapter;
import com.ateam.funshoppers.Adapters.ReviewAdapter;
import com.ateam.funshoppers.R;
import com.ateam.funshoppers.Recyclerview.GridSpacingItemDecoration;
import com.ateam.funshoppers.Recyclerview.RecyclerItemClickListener;
import com.ateam.funshoppers.Rest.ApiClient;
import com.ateam.funshoppers.Rest.ApiInterface;
import com.ateam.funshoppers.model.Business;
import com.ateam.funshoppers.model.BusinessAddress;
import com.ateam.funshoppers.model.BusinessPictures;
import com.ateam.funshoppers.model.BusinessProducts;
import com.ateam.funshoppers.model.BusinessReviews;
import com.ateam.funshoppers.model.ContactDetails;
import com.ateam.funshoppers.model.ProductFavourites;
import com.ateam.funshoppers.model.UpdateOpenHours;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ateam.funshoppers.Utility.haveNetworkConnection;

/**
 * Created by Home on 1/22/2017.
 */

public class SingleBusiness extends AppCompatActivity implements View.OnClickListener{

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private AppBarLayout appBar;
   private ImageView backdrop;
    private TextView ratingshow;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String businessName="";
    TextView BusinessName,type;
    RecyclerView productNames;
    GridView picture;
    ListView review,openhours;
    TextView contactnum,contactmail,category,websites,location,address,todayopen,verified;
    Button seephoto,seeproduct,reviewall,seeonmap,addrbtn,addphotos,button2;
    TextView titletext,ratingshow1,features,reviewfeature,pickup,delivery,appointment,customization,onlineorder;
    ImageButton callShop;
    public int width,width2;
    String reviewsnumber,favourites,ratingnumber,callNumber;
    String lat="",lng="";
    ArrayList<BusinessReviews> reviewList;
    ArrayList<BusinessProducts> productList;
    ArrayList<String> images;
    ArrayList<UpdateOpenHours.day> openHours;
    int x=0;
    ProductAdapter padapter;
    ReviewAdapter reviewAdapter;
    ImageAdapter iadapter;
    ProgressDialog progressDialog;
    ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);
     String business_id="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_business);
        findviews();
        //initializing toolbar
        init_toolbar();
        WindowManager wm=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        width=(size.x/(3));
        width2=size.x;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        if(i!=null) {
            if (i.hasExtra("id")) {
                business_id = i.getStringExtra("id");
            }
            if(i.hasExtra("name")){
                getSupportActionBar().setTitle(i.getStringExtra("name"));
            }
        }
        //gridview for pictures
        picture_grid();
        product_grid();
        review_list();



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(haveNetworkConnection(SingleBusiness.this)){
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(SingleBusiness.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }

            excute();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SingleBusiness.this, R.style.AppCompatAlertDialogStyle);
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
        reviewList=new ArrayList<>();
        reviewAdapter=new ReviewAdapter(this,reviewList);
        review.setAdapter(reviewAdapter);
     /*   openHours=new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int dayss = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayss) {
            case Calendar.MONDAY:
                x=0;
                break;
            case Calendar.TUESDAY:
                x=1;
                break;
            case Calendar.WEDNESDAY:
                x=2;
                break;
            case Calendar.THURSDAY:
                x=3;
                break;
            case Calendar.FRIDAY:
                x=4;
                break;
            case Calendar.SATURDAY:
                x=5;
                break;
            case Calendar.SUNDAY:
                x=6;
                break;
            default:
                x=0;
                // etc.
        }
        hourAdapter=new HourAdapter(this,openHours,x);
        setListViewHeightBasedOnChildren(openhours);
        openhours.setAdapter(hourAdapter);*/
    }

    private void product_grid() {
       productList=new ArrayList<>();
        padapter = new ProductAdapter(this, productList);
        GridLayoutManager mLayoutManager2 = new GridLayoutManager(this, 2);
        productNames.setLayoutManager(mLayoutManager2);
        productNames.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        productNames.setItemAnimator(new DefaultItemAnimator());
        productNames.setAdapter(padapter);
        productNames.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(SingleBusiness.this,SingleProduct.class);
                        i.putExtra("id",productList.get(position).getId());
                        startActivity(i);
                    }
                })
        );
    }

    private void picture_grid() {
        images=new ArrayList<>();
        picture.setColumnWidth(width);
        iadapter=new ImageAdapter(this,images,width);
        picture.setAdapter(iadapter);
       picture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(SingleBusiness.this, PhotoDetail.class);
                i.putExtra("photos",images);
                startActivity(i);
                return;

            }
        });

    }



    public void excute(){
        getBusinessDetails();
    }

    public void getFavourites(){
        Call<List<ProductFavourites>> call4=apiService.getBusinessFavourites(business_id);
        call4.enqueue(new Callback<List<ProductFavourites>>() {
            @Override
            public void onResponse(Call<List<ProductFavourites>> call, Response<List<ProductFavourites>> response) {
                if(response.body()!=null){
                    favourites=String.valueOf(response.body().size());
                    features.setText(String.valueOf(reviewsnumber)+" reviews | "+String.valueOf(favourites)+" favourites | "+ratingnumber+" ratings");
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;

                }
            }

            @Override
            public void onFailure(Call<List<ProductFavourites>> call, Throwable t) {
                Log.e("call 4 failed",t.toString());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;

                }

            }
        });
    }
    public void getReviews(){
        Call<List<BusinessReviews>> call3=apiService.getBusinessReviews(business_id);
        call3.enqueue(new Callback<List<BusinessReviews>>() {
            @Override
            public void onResponse(Call<List<BusinessReviews>> call, Response<List<BusinessReviews>> response) {
                if(response.body()!=null){
                    reviewList.clear();
                    for(int i=0;i<response.body().size();i++) {
                        if(!response.body().get(i).getBody().equals(""))
                            reviewList.add(response.body().get(i));
                        }
                    reviewAdapter.notifyDataSetChanged();
                    ratingnumber=String.valueOf(response.body().size());
                    reviewsnumber=String.valueOf(reviewList.size());
                    features.setText(String.valueOf(reviewsnumber)+" reviews | "+String.valueOf(favourites)+" favourites | "+ratingnumber+" ratings");
                    reviewfeature.setText(reviewsnumber+" reviews, "+ratingnumber+" ratings");
                }
                //getting favorites
                getFavourites();
            }

            @Override
            public void onFailure(Call<List<BusinessReviews>> call, Throwable t) {
                Log.e("call 3 failed",t.toString());
                //getting favorites
                getFavourites();

            }
        });
    }

    public void getProducts(){

        Call<List<BusinessProducts>> call2=apiService.getBusinessProducts(business_id);
        call2.enqueue(new Callback<List<BusinessProducts>>() {
            @Override
            public void onResponse(Call<List<BusinessProducts>> call, Response<List<BusinessProducts>> response) {
                if(response.body()!=null){
                       for (int i=0;i<response.body().size();i++){
                           if(productList.size()<2){
                               productList.add(response.body().get(i));
                           }else{
                               break;
                           }
                       }

                }else{
                    Log.e("response","null");
                }

                padapter.notifyDataSetChanged();
                //for getting reviews
                getReviews();
                }

            @Override
            public void onFailure(Call<List<BusinessProducts>> call, Throwable t) {
                Log.e("call 2","failed");
                //for getting reviews
                getReviews();

            }
        });
    }
    public void getAddress(){
        Call<List<BusinessAddress>> call6=apiService.getBusinessAddress(business_id);
        call6.enqueue(new Callback<List<BusinessAddress>>() {
            @Override
            public void onResponse(Call<List<BusinessAddress>> call, Response<List<BusinessAddress>> response) {
                if(response.body()!=null){
                    String Neighbourhood,City,Line1,Pincode,State;
                    Neighbourhood=response.body().get(0).getNeighbourhood();
                    City=response.body().get(0).getCity();
                    Line1=response.body().get(0).getLine1();
                    Pincode=response.body().get(0).getPincode();
                    State=response.body().get(0).getState();
                    lat=response.body().get(0).getLatitude();
                    lng=response.body().get(0).getLongitude();
                    location.setText(Neighbourhood+","+City);
                    address.setText(Line1+", "+Neighbourhood+", "+City+", "+State+", "+Pincode);


                }
                //for getting products
                getProducts();
            }

            @Override
            public void onFailure(Call<List<BusinessAddress>> call, Throwable t) {

                Log.e("call 6 failed",t.toString());
                //for getting products
                getProducts();
            }
        });
    }
    public void getContactDetails(){
        Call<List<ContactDetails>> call5=apiService.getContactDetails(business_id);
        call5.enqueue(new Callback<List<ContactDetails>>() {
            @Override
            public void onResponse(Call<List<ContactDetails>> call, Response<List<ContactDetails>> response) {
                if(response.body()!=null){
                   callNumber=response.body().get(0).getPhoneNumber();
                    contactmail.setText(response.body().get(0).getEmail());
                }
                //retrieving the address
                getAddress();

            }

            @Override
            public void onFailure(Call<List<ContactDetails>> call, Throwable t) {
                Log.e("call 5 failed",t.toString());

                //retrieving the address
                getAddress();

            }
        });
    }

    public void getBusinessDetails(){
        Call<Business> call1=apiService.getBusiness(business_id);
        call1.enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                if(response.body()!=null){
                    businessName=response.body().getName();
                    titletext.setText(businessName);
                    String businessDescription=response.body().getDescription();
                    BusinessName.setText(businessDescription);
                    type.setText(response.body().getType());
                  String    businessPhoto=response.body().getCoverPhotoUrl();
                   ArrayList<Integer>  categories=response.body().getCategories();
                    double rating=(response.body().getRating().doubleValue())/2.0;

                    ratingshow.setText(String.valueOf(rating)+"/5");
                    if(response.body().isAppointments()){
                        appointment.setText(R.string.available);
                    }
                    else
                    {
                        appointment.setText(R.string.navailable);
                    }
                    if(response.body().isCustomizations()){
                        customization.setText(R.string.available);
                    }
                    else
                    {
                        customization.setText(R.string.navailable);
                    }
                    if(response.body().isOnlineOrders()){
                        onlineorder.setText(R.string.available);
                    }
                    else
                    {
                        onlineorder.setText(R.string.navailable);
                    }
                    if(images.size()==0){
                        images.add(0,businessPhoto);}
                    else if(!images.get(0).equals(businessPhoto)){
                        images.add(0,businessPhoto);
                    }
                    iadapter.notifyDataSetChanged();

                    if(!response.body().isVerified()){
                        verified.setVisibility(TextView.VISIBLE);
                    }
                    //for getting the open hours
                    openHours=response.body().getOpenHours();
                  //  hourAdapter.notifyDataSetChanged();
                    if(openHours!=null){
                        //for changing colour of textview

                        //for setting todayopen
                        if(TextUtils.isEmpty(todayopen.getText())){
                            todayopen.append(openHours.get(x).getStarttime()+" to "+openHours.get(x).getEndtime()+"[Today]");
                        }

                    }
                    Picasso.with(SingleBusiness.this).load(response.body().getCoverPhotoUrl()).resize(backdrop.getWidth(),
                            backdrop.getHeight()).into(backdrop);
                    switch (categories.get(0)){
                        case 1:category.setText(R.string.fashion);
                            break;
                        case 2:
                            category.setText(R.string.food);
                            break;
                        case 3:
                            category.setText("Home & Living");
                            break;
                        case 4:
                            category.setText("Personalized");
                            break;
                        case 5:
                            category.setText("Organic");
                            break;
                        case 6:
                            category.setText("Kids");
                            break;
                        default:
                            break;
                    }
                }
                //for pictures
                populategrid();


            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                Log.e("call 1 failed",t.toString());
                //for pictures
                populategrid();


            }
        });

    }
    public void populategrid(){
        //for getting business pictures
        Call<List<BusinessPictures>> call=apiService.getBusinessPictures(business_id);
        call.enqueue(new Callback<List<BusinessPictures>>() {
            @Override
            public void onResponse(Call<List<BusinessPictures>> call, Response<List<BusinessPictures>> response) {
               for (int i=0;i<response.body().size();i++){
                   images.add(response.body().get(i).getUrl());
               }
              iadapter.notifyDataSetChanged();
                //for contact details
                getContactDetails();

            }


            @Override
            public void onFailure(Call<List<BusinessPictures>> call, Throwable t) {
                Log.e("call failed",t.toString());
                iadapter.notifyDataSetChanged();
                //for contact details
                getContactDetails();

            }
        });

    }

    private void init_toolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        //collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        // collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        OnOffsetChangedListener mListener = new OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(collapsingToolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
                    ratingshow.setVisibility(View.INVISIBLE);

                } else {
                    ratingshow.setVisibility(View.VISIBLE);

                }
            }
        };

        appBar.addOnOffsetChangedListener(mListener);

    }



    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
          case R.id.photosee:
                Intent i=new Intent(this, PhotoDetail.class);
                i.putExtra("photos",images);
                startActivity(i);
                return;
          case R.id.productsee:
                Intent i1=new Intent(this, ProductDetail.class);
                i1.putExtra("id",business_id);
              i1.putExtra("name",businessName);
                startActivity(i1);
                return;
            case R.id.reviewall:
                Intent i2=new Intent(this, ReviewDetail.class);
                i2.putExtra("id",business_id);


                startActivity(i2);
                return;
            case R.id.callshop:
                Intent i3=new Intent(Intent.ACTION_DIAL);
                i3.setData(Uri.parse("tel:"+callNumber));
                startActivity(i3);
                return;
          case R.id.seeonmap:
              GPSTracker gps = new GPSTracker(SingleBusiness.this);
              if (gps.canGetLocation()) {
                Intent i4=new Intent(this,MapsActivity.class);
                i4.putExtra("lat",Double.parseDouble(lat));
                i4.putExtra("lng",Double.parseDouble(lng));
                startActivity(i4);
              } else {
                  gps.showSettingsAlert();

              }

              return;
            case R.id.backdrop:
                Intent i5=new Intent(this,SlideshowDialogFragment.class);
                i5.putExtra("images",images);
                i5.putExtra("position",0);
                startActivity(i5);
                return;
        }


    }

    public void findviews(){
        backdrop=(ImageView)findViewById(R.id.backdrop);
        ratingshow=(TextView)findViewById(R.id.ratingshow);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        appBar=(AppBarLayout)findViewById(R.id.appbar);
        //////
        BusinessName=(TextView)findViewById(R.id.businessname);
        type=(TextView)findViewById(R.id.type);
        features=(TextView)findViewById(R.id.features);
        reviewfeature=(TextView)findViewById(R.id.reviewfeature);
        contactmail=(TextView)findViewById(R.id.contactmail);
        category=(TextView)findViewById(R.id.category);
        location=(TextView)findViewById(R.id.location);
        address=(TextView)findViewById(R.id.address);
        todayopen=(TextView)findViewById(R.id.todayopen);
        verified=(TextView)findViewById(R.id.verified);
        appointment=(TextView)findViewById(R.id.appointment);
        customization=(TextView)findViewById(R.id.customization);
        onlineorder=(TextView)findViewById(R.id.onlineorder);
        callShop=(ImageButton)findViewById(R.id.callshop);
        ratingshow=(TextView)findViewById(R.id.ratingshow);
        backdrop=(ImageButton) findViewById(R.id.backdrop);
        titletext=(TextView)findViewById(R.id.love_music);
        //initializing see all buttons
        seephoto=(Button)findViewById(R.id.photosee);
        seeproduct=(Button)findViewById(R.id.productsee);
        reviewall=(Button)findViewById(R.id.reviewall);
        seeonmap=(Button)findViewById(R.id.seeonmap);
        //grids
        picture=(GridView)findViewById(R.id.gridview);
        productNames=(RecyclerView) findViewById(R.id.listview);
        review=(ListView)findViewById(R.id.reviews);
        //openhours=(ListView)findViewById(R.id.opentime);
        callShop.setOnClickListener(this);
        seeonmap.setOnClickListener(this);
        reviewall.setOnClickListener(this);
        backdrop.setOnClickListener(this);
        seephoto.setOnClickListener(this);
        seeproduct.setOnClickListener(this);
    }
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
