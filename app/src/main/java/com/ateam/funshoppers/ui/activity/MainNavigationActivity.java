/*
 *
 *  Copyright (c) 2015 SameBits UG. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ateam.funshoppers.ui.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.ateam.funshoppers.BeaconDistance.BeaconDisctance;
import com.ateam.funshoppers.BeaconLocatorApp;
import com.ateam.funshoppers.Main_navigation.MainActivity;
import com.ateam.funshoppers.R;
import com.ateam.funshoppers.model.TrackedBeacon;
import com.ateam.funshoppers.ui.adapter.DetectedBeaconAdapter;
import com.ateam.funshoppers.ui.fragment.DetectedBeaconsFragment;
import com.ateam.funshoppers.ui.fragment.ScanFragment;
import com.ateam.funshoppers.ui.fragment.ScanRadarFragment;
import com.ateam.funshoppers.ui.fragment.TrackedBeaconsFragment;
import com.ateam.funshoppers.util.Constants;
import com.ateam.funshoppers.util.DialogBuilder;

import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainNavigationActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textView;
    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    int backButtonCount=0;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    BeaconManager mBeaconManager;
    BluetoothAdapter bluetoothAdapter;
    TrackedBeacon mBeacon;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainNavigationActivity.class);
    }

    @OnClick(R.id.fab)
    void navAction() {

        Fragment currentFragment = getFragmentInstance(R.id.content_frame);
        String tag = currentFragment.getTag();
        switch (tag) {
            case Constants.TAG_FRAGMENT_SCAN_LIST:
            case Constants.TAG_FRAGMENT_SCAN_RADAR:
                ((ScanFragment) currentFragment).scanStartStopAction();
                break;
            case Constants.TAG_FRAGMENT_TRACKED_BEACON_LIST:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        ButterKnife.bind(this);

        setupToolbar();

        navigationView.setNavigationItemSelectedListener(this);

        mBeaconManager = BeaconLocatorApp.from(this).getComponent().beaconManager();

        checkPermissions();
        verifyBluetooth();

        readExtras();

        if (null == savedInstanceState || mBeacon != null) {
            launchTrackedListView();
        }

    }

    protected void readExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mBeacon = intent.getExtras().getParcelable(Constants.ARG_BEACON);
        }
    }

    private void setupToolbar() {

        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(Constants.TAG, "coarse location permission granted");
                } else {
                    final Dialog permFailedDialog = DialogBuilder.createSimpleOkErrorDialog(
                            this,
                            getString(R.string.dialog_error_functionality_limited),
                            getString(R.string.error_message_location_access_not_granted)
                    );
                    permFailedDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //finish();
                        }
                    });
                    permFailedDialog.show();
                }
                return;
            }
        }
    }

    @TargetApi(23)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                final Dialog permDialog = DialogBuilder.createSimpleOkErrorDialog(
                        this,
                        getString(R.string.dialog_error_need_location_access),
                        getString(R.string.error_message_location_access_need_tobe_granted)
                );
                permDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                permDialog.show();
            }
        }
    }

    @TargetApi(18)
    private void verifyBluetooth() {

        try {
            if (!mBeaconManager.checkAvailability()) {

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, RESULT_OK);

            }
        } catch (RuntimeException e) {

            final Dialog bleDialog = DialogBuilder.createSimpleOkErrorDialog(
                    this,
                    getString(R.string.dialog_error_ble_not_supported),
                    getString(R.string.error_message_bluetooth_le_not_supported)
            );
            bleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }
            });
            bleDialog.show();
        }
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation ui item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_scan_radar:
                launchRadarScanView();
                break;
            case R.id.Main:
                launchMain();
                break;
            case R.id.nav_scan_around:
                launchScanBeaconView();
                break;

            case R.id.nav_tracked_list:
                launchTrackedListView();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void addScanBeaconFragment() {
      /*  FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (checkFragmentInstance(R.id.content_frame, DetectedBeaconsFragment.class) == null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, DetectedBeaconsFragment.newInstance(), Constants.TAG_FRAGMENT_SCAN_LIST)
                        .commit();
            }
        }*/
      Intent intent=new Intent(MainNavigationActivity.this, BeaconDisctance.class);
        startActivity(intent);
    }

    private void addRadarScanFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (checkFragmentInstance(R.id.content_frame, ScanRadarFragment.class) == null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, ScanRadarFragment.newInstance(), Constants.TAG_FRAGMENT_SCAN_RADAR)
                        .commit();
            }
        }
    }

    private void addMainFragment() {


    }

    private void addTrackedListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment frg = checkFragmentInstance(R.id.content_frame, TrackedBeaconsFragment.class);
            if (frg == null) {
                TrackedBeaconsFragment tFrg = TrackedBeaconsFragment.newInstance();
                if (mBeacon != null) {
                    Bundle bundles = new Bundle();
                    bundles.putParcelable(Constants.ARG_BEACON, mBeacon);
                    tFrg.setArguments(bundles);
                }
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, tFrg, Constants.TAG_FRAGMENT_TRACKED_BEACON_LIST)
                        .commit();
            }
        }
    }

    public void hideFab() {
        fab.setVisibility(View.GONE);
    }

    public void swappingFabAway() {
        fab.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pop_down);
        fab.startAnimation(animation);
    }

    public void swappingFabUp() {
        fab.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pop_up);
        fab.startAnimation(animation);
    }

    public void swappingFloatingScanIcon(boolean isScanning) {
        if (isScanning) {
            setFabIcon(R.drawable.ic_portable_wifi_off_white_24dp);
        } else {
            setFabIcon(R.drawable.ic_track_changes_white_24dp);
        }
    }

    public void swappingFloatingIcon() {
        Fragment currentFragment = getFragmentInstance(R.id.content_frame);
        String tag = currentFragment.getTag();
        switch (tag) {
            case Constants.TAG_FRAGMENT_SCAN_LIST:
            case Constants.TAG_FRAGMENT_SCAN_RADAR:
                setFabIcon(R.drawable.ic_track_changes_white_24dp);
                break;
            default:
                setFabIcon(R.drawable.ic_add_white_24dp);
                hideFab();
        }
    }

    private void setFabIcon(final int resId) {
        fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                fab.setImageResource(resId);
                fab.show();
            }
        });
    }

    private void launchScanBeaconView() {
        addScanBeaconFragment();
    }

    private void launchMain() {
        addMainFragment();
    }

    private void launchRadarScanView() {
        addRadarScanFragment();
    }

    private void launchTrackedListView() {
        addTrackedListFragment();
    }
    @Override

    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to go to Store.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


    }

