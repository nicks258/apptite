package com.ateam.funshoppers.AppIntro;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ateam.funshoppers.Main_navigation.LoginActivity;
import com.ateam.funshoppers.R;
import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

public class ImageBackgroundExampleActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Phygit Technology", "Beacon for mobile marketing and e-commerce.", R.drawable.ic_access_point_network);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Search by location", "First location based product and pricing comparison app with trusted retailers.", R.drawable.ic_add_location_white_24dp);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Get Cashback", "Earn cashback each time when you make purchase with us.", R.drawable.reward);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
        }

        setFinishButtonTitle("Get Started");
        showNavigationControls(true);
        //setGradientBackground();
        setImageBackground(R.drawable.appintrobackp);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setFont(face);

        setInactiveIndicatorColor(R.color.grey_600);
        setActiveIndicatorColor(R.color.white);

        setOnboardPages(pages);

    }

    @Override
    public void onFinishButtonPressed() {
        Intent intent = new Intent(ImageBackgroundExampleActivity.this, LoginActivity.class);
        startActivity(intent);
        Log.d("ImageBackground","Sumit");
    }
}
