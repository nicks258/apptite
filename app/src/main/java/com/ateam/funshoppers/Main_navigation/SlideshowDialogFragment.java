package com.ateam.funshoppers.Main_navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.ateam.funshoppers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Home on 12/13/2016.
 */

public class SlideshowDialogFragment extends AppCompatActivity implements GestureDetector.OnGestureListener,View.OnSystemUiVisibilityChangeListener {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<String> images;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private View mdecorView;
    private MyViewPagerAdapter myViewPagerAdapter;
    private Animation mSlideUp;
    private Animation mSlideDown;
    private int selectedPosition = 0;
    private int mCurrentPosition;
    private final Handler mLeanBackHandler = new Handler();
    private int mLastSystemUIVisibility;
    private final Runnable mEnterLeanback = new Runnable() {
        @Override
        public void run() {
            enableFullScreen(true);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //decor view
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(this);
      //  getWindow().getDecorView().setOnClickListener(this);
        enableFullScreen(true);
        setContentView(R.layout.fragment_image_slider);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
       // setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        //recieving intents
        images = intent.getStringArrayListExtra("images");
        selectedPosition = intent.getIntExtra("position", 0);


        if (images.size() > 0) {
            images.add(0, images.get(images.size() - 1));
            images.add(images.get(1));


            selectedPosition = selectedPosition + 1;


            Log.e(TAG, "position: " + selectedPosition);
            Log.e(TAG, "images size: " + images.size());
        }

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

    }

    protected void enableFullScreen(boolean enabled) {
        int newVisibility =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if(enabled) {
            newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN
                    |  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Want to hide again after 3s
        if(!enabled) {
            resetHideTimer();
        }

        // Set the visibility
       getWindow().getDecorView().setSystemUiVisibility(newVisibility);
    }

    private void resetHideTimer() {
        // First cancel any queued events - i.e. resetting the countdown clock
        mLeanBackHandler.removeCallbacks(mEnterLeanback);
        // And fire the event in 3s time
        mLeanBackHandler.postDelayed(mEnterLeanback, 3000);
    }
    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if((mLastSystemUIVisibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0
                && (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
            resetHideTimer();
        }
        mLastSystemUIVisibility = visibility;
    }





    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
       // displayMetaInfo(selectedPosition);
    }
    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mCurrentPosition=position;

            //displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // For going from the first item to the last item, when the 1st A goes to 1st C on the left, again we let the ViewPager do it's job until the movement is completed, we then set the current item to the 2nd C.
            // Set the current item to the item before the last item if the current position is 0
            if (mCurrentPosition == 0)                  viewPager.setCurrentItem(images.size() - 2, false); // lastPageIndex is the index of the last item, in this case is pointing to the 2nd A on the list. This variable should be declared and initialzed as a global variable

            // For going from the last item to the first item, when the 2nd C goes to the 2nd A on the right, we let the ViewPager do it's job for us, once the movement is completed, we set the current item to the 1st A.
            // Set the current item to the second item if the current position is on the last
            if (mCurrentPosition == images.size()-1)      viewPager.setCurrentItem(1, false);


        }
    };


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        resetHideTimer();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            String image = images.get(position);

           /* Glide.with(getActivity()).load(image.getLarge())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);*/
            Picasso.with(SlideshowDialogFragment.this).load(image).placeholder(R.drawable.loading).into(imageViewPreview);


            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }



        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}


