package com.ateam.funshoppers.Main_navigation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.ateam.funshoppers.Adapters.BusinessAdapter;
import com.ateam.funshoppers.R;
import com.ateam.funshoppers.Recyclerview.GridSpacingItemDecoration;
import com.ateam.funshoppers.Recyclerview.RecyclerItemClickListener;
import com.ateam.funshoppers.Rest.ApiClient;
import com.ateam.funshoppers.Rest.ApiInterface;
import com.ateam.funshoppers.model.Businesses;
import com.ateam.funshoppers.ui.activity.MainNavigationActivity;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,BaseSliderView.OnSliderClickListener {
    Toolbar toolbar;
    LocalDatabase localDatabase;
    RecyclerView fashiongrid,foodgrid,lifestylegrid;;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Button fashionsee,foodsee,lifestylesee;
    BusinessAdapter fashionAdapter,foodadapter,lifestyleadapter;
    ArrayList<Businesses> fashionList,foodList,lifeStyleList,popular;
    ImageView ib1,ib2,ib3;
    private SliderLayout mDemoSlider;
    public static int width,width2;
    ProgressDialog progressDialog;

    ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);
    public void findview(){
        //actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        //imagebuttons
        ib1=(ImageView) findViewById(R.id.bigpic);
        ib2=(ImageView) findViewById(R.id.smallpic1);
        ib3=(ImageView) findViewById(R.id.smallpic2);
        ib1.setOnClickListener(this);
        ib2.setOnClickListener(this);
        ib3.setOnClickListener(this);
        //initializing gridviews
        fashiongrid=(RecyclerView) findViewById(R.id.fashiongrid);
        foodgrid=(RecyclerView) findViewById(R.id.foodgrid);
        lifestylegrid=(RecyclerView) findViewById(R.id.lifestylegrid);
        //buttons
        fashionsee=(Button)findViewById(R.id.fashionsee);
        foodsee=(Button)findViewById(R.id.foodsee);
        lifestylesee=(Button)findViewById(R.id.lifestylesee);
        //slider
        mDemoSlider=(SliderLayout)findViewById(R.id.view_pager);
        //onclick
        fashionsee.setOnClickListener(this);
        foodsee.setOnClickListener(this);
        lifestylesee.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       findview();
        setSupportActionBar(toolbar);
        navigationcontrol();
        init_slider();

        // viewpagerontrol();
        WindowManager wm=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        width=(int)(size.x/(5));
        width2=size.x;
        fashionList=new ArrayList<>();
        foodList=new ArrayList<>();
        lifeStyleList=new ArrayList<>();
        popular=new ArrayList<>();
        setFashiongrid();
        setFoodgrid();
        setLifestylegrid();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(haveNetworkConnection()){
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
            excute();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
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
    protected void onStop() {

        super.onStop();
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void init_slider() {
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("banner 1",R.drawable.banner1);
        file_maps.put("banner 2",R.drawable.banner2);
        file_maps.put("banner 3",R.drawable.banner3);
        file_maps.put("banner 4",R.drawable.banner4);
        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView

                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(6000);
     /* mDemoSlider.addOnPageChangeListener(this);
     ListView l = (ListView)findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
                Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    public void setFashiongrid(){
        fashionAdapter = new BusinessAdapter(this, fashionList);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this,2);
        fashiongrid.setLayoutManager(mLayoutManager1);
       fashiongrid.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        fashiongrid.setItemAnimator(new DefaultItemAnimator());
        fashiongrid.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(MainActivity.this,SingleBusiness.class);
                        i.putExtra("id",fashionList.get(position).getId());
                        i.putExtra("name",fashionList.get(position).getName());
                        startActivity(i);
                    }
                })
        );
        fashiongrid.setAdapter(fashionAdapter);

    }
    public void setFoodgrid(){
        foodadapter = new BusinessAdapter(this, foodList);
        GridLayoutManager mLayoutManager2 = new GridLayoutManager(this, 2);
        foodgrid.setLayoutManager(mLayoutManager2);
     foodgrid.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        foodgrid.setItemAnimator(new DefaultItemAnimator());
        foodgrid.setAdapter(foodadapter);
        foodgrid.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(MainActivity.this,SingleBusiness.class);
                        i.putExtra("id",foodList.get(position).getId());
                        i.putExtra("name",foodList.get(position).getName());
                        startActivity(i);
                    }
                })
        );

    }
    public void setLifestylegrid(){
        lifestyleadapter = new BusinessAdapter(this, lifeStyleList);
        GridLayoutManager mLayoutManager3 = new GridLayoutManager(this, 2);
        lifestylegrid.setLayoutManager(mLayoutManager3);
   lifestylegrid.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        lifestylegrid.setItemAnimator(new DefaultItemAnimator());
        lifestylegrid.setAdapter(lifestyleadapter);
        lifestylegrid.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(MainActivity.this,SingleBusiness.class);
                        i.putExtra("id",lifeStyleList.get(position).getId());
                        i.putExtra("name",lifeStyleList.get(position).getName());
                        startActivity(i);
                    }
                })
        );

    }

    public void excute(){

        Call<List<Businesses>> getFashionBusiness=apiService.getBusinesses();
        getFashionBusiness.enqueue(new Callback<List<Businesses>>() {
            @Override
            public void onResponse(Call<List<Businesses>> call, Response<List<Businesses>> response) {
               fashionList.clear();
                foodList.clear();
                lifeStyleList.clear();
                popular.clear();
                popular.add(response.body().get(0));
                popular.add(response.body().get(1));
                popular.add(response.body().get(2));
                Picasso.with(MainActivity.this).load(popular.get(0).getCoverPhotoUrl()).resize(ib1.getWidth(),ib1.getHeight()).placeholder(R.drawable.loading).into(ib1);
                Picasso.with(MainActivity.this).load(popular.get(1).getCoverPhotoUrl()).resize(ib2.getWidth(),ib2.getHeight()).placeholder(R.drawable.loading).into(ib2);
                Picasso.with(MainActivity.this).load(popular.get(2).getCoverPhotoUrl()).resize(ib2.getWidth(),ib2.getHeight()).placeholder(R.drawable.loading).into(ib3);
                for(int i=0;i<response.body().size();i++){
                    if(response.body().get(i).getCategories().contains(1)){
                        if(fashionList.size()<2){
                            fashionList.add(response.body().get(i));
                        }
                        else{
                            if(foodList.size()>3 && lifeStyleList.size()>3)
                                break;
                        }
                    }
                    if(response.body().get(i).getCategories().contains(2)){
                        if(foodList.size()<2){
                            foodList.add(response.body().get(i));
                        }
                    }
                    if(response.body().get(i).getCategories().contains(3)){
                        if(lifeStyleList.size()<2){
                            lifeStyleList.add(response.body().get(i));
                        }
                    }
                }

                fashionAdapter.notifyDataSetChanged();
                foodadapter.notifyDataSetChanged();
                lifestyleadapter.notifyDataSetChanged();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;

                }

            }

            @Override
            public void onFailure(Call<List<Businesses>> call, Throwable t) {
                Log.e("getFashioBusinessFailed",t.toString());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;

                }

            }
        });

    }


    public void navigationcontrol(){
    //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            localDatabase = new LocalDatabase(MainActivity.this);
            //Initializing NavigationView

            Contact contact = localDatabase.getLoggedInUser();
           // da = contact.email;
            //textView.setText("Hi"+" "+contact.name );
            //Checking if the item is in checked state or not, if not make it in checked state
            if(menuItem.isChecked()) menuItem.setChecked(false);
            else menuItem.setChecked(true);

            //Closing drawer on item click
            drawerLayout.closeDrawers();

            //Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()){
                case R.id.fashion:
                    Intent i=new Intent(MainActivity.this,BusinessList.class);
                    i.putExtra("type","fashion");
                    startActivity(i);
                    return true;
                case R.id.food:
                    Intent i1=new Intent(MainActivity.this,BusinessList.class);
                    i1.putExtra("type","food");
                    startActivity(i1);
                    return true;
                case R.id.lifestyle:
                    Intent i2=new Intent(MainActivity.this,BusinessList.class);
                    i2.putExtra("type","lifestyle");
                    startActivity(i2);
                    return true;
                case R.id.featured:
                    Intent i3=new Intent(MainActivity.this, Featured.class);
                    i3.putExtra("url","http://suvojitkar365.esy.es/apptite/details.php?id=1");
                    i3.putExtra("title","Featured");
                    startActivity(i3);
                    return true;
                case R.id.offers:
                    Intent i4=new Intent(MainActivity.this,Featured.class);
                    i4.putExtra("url","http://suvojitkar365.esy.es/apptite/offerzone.php");
                    i4.putExtra("title","Offer Zone");
                    startActivity(i4);
                    return true;
                case R.id.faq:
                    Intent i5=new Intent(MainActivity.this,Featured.class);
                    i5.putExtra("url","http://suvojitkar365.esy.es/apptite/faq.php");
                    i5.putExtra("title","FAQ");
                    startActivity(i5);
                    return true;
                case R .id.trial:
                    Intent i6=new Intent(MainActivity.this, Trial.class);
                    startActivity(i6);
                    return true;
                case R.id.gostore:
                    Intent  intent = new Intent(MainActivity.this, MainNavigationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.contact:
                    Intent intent2 = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "funshoppers258@gmail.com"));
                    intent2.putExtra(Intent.EXTRA_SUBJECT, "Query");

                    startActivity(intent2);
                    return  true;
                case R.id.logout:
                    localDatabase.clearData();
                    localDatabase.setUserLoggedIn(false);

                    Intent intent3 = new Intent(MainActivity.this , LoginActivity.class);
                    startActivity(intent3);

                default:
                    return true;
            }
        }
    });
    // Initializing Drawer Layout and ActionBarToggle

    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

        @Override
        public void onDrawerClosed(View drawerView) {
            // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
            super.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

            super.onDrawerOpened(drawerView);
        }
    };

    //Setting the actionbarToggle to drawer layout
    drawerLayout.setDrawerListener(actionBarDrawerToggle);

    //calling sync state is necessay or else your hamburger icon wont show up
    actionBarDrawerToggle.syncState();

}
    public void viewpagerontrol(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.fashionsee:
                Intent i=new Intent(MainActivity.this, BusinessList.class);
                i.putExtra("business",true);
                i.putExtra("type","fashion");
                startActivity(i);
                return;
            case R.id.foodsee:
                Intent i1=new Intent(MainActivity.this, BusinessList.class);
                i1.putExtra("business",true);
                i1.putExtra("type","food");
                startActivity(i1);
                return;
            case R.id.lifestylesee:
                Intent i2=new Intent(MainActivity.this, BusinessList.class);
                i2.putExtra("business",true);
                i2.putExtra("type","lifestyle");
                startActivity(i2);
                return;
            case R.id.bigpic:
                Intent i3=new Intent(MainActivity.this, SingleBusiness.class);
                i3.putExtra("id",popular.get(0).getId());
                i3.putExtra("name",popular.get(0).getName());
                startActivity(i3);
                return;
            case R.id.smallpic1:
                Intent i4=new Intent(MainActivity.this, SingleBusiness.class);
                i4.putExtra("id",popular.get(1).getId());
                i4.putExtra("name",popular.get(1).getName());
                startActivity(i4);
                return;
            case R.id.smallpic2:
                Intent i5=new Intent(MainActivity.this, SingleBusiness.class);
                i5.putExtra("id",popular.get(2).getId());
                i5.putExtra("name",popular.get(2).getName());
                startActivity(i5);
                return;

        }

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }




    /*---------------
    ----------------------------------------------------------------------------
     */

    private class ImagePagerAdapter extends PagerAdapter {
        private int[] mImages = new int[] {
               R.drawable.banner1
        };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = MainActivity.this;
            final ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(mImages[position]);
            ((ViewPager) container).addView(imageView, 0);

            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                  //  onClickBrowsePage(imageView);
                }
            });
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }


    /**
     * Converting dp to pixel
     */
    public  int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}








