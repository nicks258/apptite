package com.ateam.funshoppers.Main_navigation;

<<<<<<< HEAD
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
=======
import android.content.Intent;
import android.os.Bundle;
>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ateam.funshoppers.R;
import com.ateam.funshoppers.ui.activity.MainNavigationActivity;
<<<<<<< HEAD
import com.rey.material.widget.ProgressView;

import java.util.List;



public class MainActivity extends AppCompatActivity {


    LocalDatabase localDatabase;
    //Defining Variables
    private Toolbar toolbar;



    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    Boolean doubleBackToExitPressedOnce = false;
=======

public class MainActivity extends AppCompatActivity {
    LocalDatabase localDatabase;
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_main);

<<<<<<< HEAD

=======
>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        localDatabase = new LocalDatabase(this);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


<<<<<<< HEAD



=======
>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc
                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){



                    case R.id.inbox:
                       // Toast.makeText(getApplicationContext(),"Inbox Selected", Toast.LENGTH_SHORT).show();
                        Home_page fragment = new Home_page();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,fragment);
                        fragmentTransaction.commit();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.starred:
                        Toast.makeText(getApplicationContext(),"Stared Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.sent_mail:
                        Toast.makeText(getApplicationContext(),"Send Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drafts:
                        Toast.makeText(getApplicationContext(),"Drafts Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.allmail:
                        Toast.makeText(getApplicationContext(),"All Mail Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.trash:
                        Toast.makeText(getApplicationContext(),"Trash Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.spam:
                        Toast.makeText(getApplicationContext(),"Spam Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });



        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_save) {
            Toast.makeText(this,"Save",Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id==R.id.logout)
        {
            localDatabase.clearData();
            localDatabase.setUserLoggedIn(false);

            Intent intent = new Intent(this , LoginActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_bookmark)
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
        Contact contact = localDatabase.getLoggedInUser();
        Home_page fragment = new Home_page();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }


<<<<<<< HEAD
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
=======
>>>>>>> de57d8b7fc7ce69b397cdc65b2db21e023c503dc
}