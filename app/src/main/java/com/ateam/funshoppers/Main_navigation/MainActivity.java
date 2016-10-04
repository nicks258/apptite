package com.ateam.funshoppers.Main_navigation;

import android.net.Uri;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ateam.funshoppers.R;
import com.ateam.funshoppers.ui.activity.MainNavigationActivity;


public class MainActivity extends AppCompatActivity {
    LocalDatabase localDatabase;
    //Defining Variables
    private Toolbar toolbar;
    View view;
    String da;
    int backButtonCount=0;
    LayoutInflater mInflater;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_main);

        TextView textView = (TextView) findViewById(R.id.user_name);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        localDatabase = new LocalDatabase(this);
        //Initializing NavigationView

        Contact contact = localDatabase.getLoggedInUser();
        da = contact.email;
        textView.setText("Hi"+" "+contact.name );

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {



                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){



                    case R.id.home:
                       // Toast.makeText(getApplicationContext(),"Inbox Selected", Toast.LENGTH_SHORT).show();
                        Home_page fragment = new Home_page();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,fragment);
                        fragmentTransaction.commit();
                        return true;


                    case R.id.electronics:
                        Electronics electronics = new Electronics();
                        android.support.v4.app.FragmentTransaction electronicsTransaction = getSupportFragmentManager().beginTransaction();
                        electronicsTransaction.replace(R.id.frame,electronics);
                        electronicsTransaction.commit();                        return true;
                    case R.id.lifestyle:
                        Lifestyle lifestyle = new Lifestyle();
                        android.support.v4.app.FragmentTransaction lifestyleTransaction = getSupportFragmentManager().beginTransaction();
                        lifestyleTransaction.replace(R.id.frame,lifestyle);
                        lifestyleTransaction.commit();                        return true;
                    case R.id.homes:
                        Homes homes = new Homes();
                        android.support.v4.app.FragmentTransaction homesTransaction = getSupportFragmentManager().beginTransaction();
                        homesTransaction.replace(R.id.frame,homes);
                        homesTransaction.commit();                        return true;
                    case R.id.books:
                        Brands brands = new Brands();
                        android.support.v4.app.FragmentTransaction brandsTransaction = getSupportFragmentManager().beginTransaction();
                        brandsTransaction.replace(R.id.frame,brands);
                        brandsTransaction.commit();                        return true;

                    case R.id.offer:
                        Offerzone offerzone = new Offerzone();
                        android.support.v4.app.FragmentTransaction offerTransaction = getSupportFragmentManager().beginTransaction();
                        offerTransaction.replace(R.id.frame,offerzone);
                        offerTransaction.commit();                        return true;
                    case R.id.mycart:
                        MyCart myCart = new MyCart();
                        android.support.v4.app.FragmentTransaction cartTransaction = getSupportFragmentManager().beginTransaction();
                        cartTransaction.replace(R.id.frame,myCart);
                        cartTransaction.commit();
                        return true;
                    case R.id.myaccountt:
                        MyAccount myAccount = new MyAccount();
                        android.support.v4.app.FragmentTransaction myaccoutTransaction = getSupportFragmentManager().beginTransaction();
                        myaccoutTransaction.replace(R.id.frame,myAccount);
                        myaccoutTransaction.commit();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Went Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });



        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.myaccount) {
            MyCart myCart = new MyCart();
            android.support.v4.app.FragmentTransaction cartTransaction = getSupportFragmentManager().beginTransaction();
            cartTransaction.replace(R.id.frame,myCart);
            cartTransaction.commit();
            return true;
        }
        else if(id==R.id.logout)
        {
            localDatabase.clearData();
            localDatabase.setUserLoggedIn(false);

            Intent intent = new Intent(this , LoginActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.faq)
        {
            Faq faq = new Faq();
            android.support.v4.app.FragmentTransaction faqTransaction = getSupportFragmentManager().beginTransaction();
            faqTransaction.replace(R.id.frame,faq);
            faqTransaction.commit();
            return true;
        }
        else if(id==R.id.contactus)
        {
            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "funshoppers258@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query");

            startActivity(intent);
        }
        else if(id == R.id.gotomall)
        {
            Intent  intent = new Intent(MainActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();

        if(authenticate() == true)
        {
            displayContactDetails();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(intent);
        }
    }

    private boolean authenticate()
    {
        return localDatabase.getUserLoggedIn();
    }

    private void displayContactDetails()
    {


        Home_page fragment = new Home_page();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    @Override

    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

}
