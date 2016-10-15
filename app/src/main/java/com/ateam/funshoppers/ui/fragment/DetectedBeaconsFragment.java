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

package com.ateam.funshoppers.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.ateam.funshoppers.R;
import com.ateam.funshoppers.model.DetectedBeacon;
import com.ateam.funshoppers.model.IManagedBeacon;
import com.ateam.funshoppers.model.TrackedBeacon;
import com.ateam.funshoppers.ui.activity.MainNavigationActivity;
import com.ateam.funshoppers.ui.adapter.BeaconAdapter;
import com.ateam.funshoppers.ui.adapter.DetectedBeaconAdapter;
import com.ateam.funshoppers.ui.view.ContextMenuRecyclerView;
import com.ateam.funshoppers.ui.view.DividerItemDecoration;
import com.ateam.funshoppers.util.Constants;
import com.ateam.funshoppers.util.PreferencesUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;



public class DetectedBeaconsFragment extends ScanFragment implements BeaconAdapter.OnBeaconLongClickListener {

    //40 sec timeout for scanning
    static final int SCAN_TIMEOUT = 40000;
    protected CountDownTimer mTimer;
    @Bind(R.id.recycler_detected_beacons)
    ContextMenuRecyclerView mListBeacons;
    @Bind(R.id.progress_indicator)
    ProgressBar mProgressBar;
    @Bind(R.id.empty_scan_view)
    ViewStub mEmpty;
    EmptyView mEmptyView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private DetectedBeaconAdapter mBeaconsAdapter;
    TextView textView;
    public static DetectedBeaconsFragment newInstance() {
        DetectedBeaconsFragment beaconsFragment = new DetectedBeaconsFragment();
        return beaconsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBeaconsAdapter = new DetectedBeaconAdapter(this);
        DetectedBeacon.getDistanceCalculator();

        mBeaconsAdapter.setOnBeaconLongClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_scan_beacons, container, false);
        ButterKnife.bind(this, fragmentView);

       // textView = (TextView)fragmentView.findViewById(R.id.beacon_item_uuid_value);
       // textView.setText(dBeacon.getUUID().substring(7));
       // textView.setText("Yes i am he");
        Log.d("Sumit","ioo");
        setupToolbar();
        setupRecyclerView();
        setupTimer();

        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimer.cancel();
        ButterKnife.unbind(this);
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.title_fragment_detected_beacons);
        }
    }

    private void setupRecyclerView() {
        View viewFromEmpty = mEmpty.inflate();
        mEmptyView = new EmptyView(viewFromEmpty);
        mEmptyView.text.setText(getString(R.string.text_please_start_scan));

        mListBeacons.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListBeacons.setHasFixedSize(true);
        mListBeacons.addItemDecoration(new DividerItemDecoration(getActivity()));
        mListBeacons.setAdapter(mBeaconsAdapter);

        registerForContextMenu(mListBeacons);
        Log.d("god",Integer.toString(viewFromEmpty.getId()));
        Log.d("god",Integer.toString(mListBeacons.getId()));
        mProgressBar.setVisibility(View.GONE);

    }

    private void setupTimer() {
        mTimer = new CountDownTimer(SCAN_TIMEOUT, PreferencesUtil.getManualScanTimeout(getApplicationContext())) {
            public void onFinish() {
                stopScanTimeout();
            }

            public void onTick(long tick) {
               // Log.d("lion",Long.toString(tick));
            }
        };
    }

    private void stopScanTimeout() {
        stopScan();
        mEmptyView.text.setText(getString(R.string.text_scan_not_found));
    }

    private void emptyListSetup() {
        if (mBeaconsAdapter.getItemCount() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
            mEmptyView.text.setText(getString(R.string.text_please_start_scan));
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void startScan() {
        mProgressBar.setVisibility(View.VISIBLE);
        mEmpty.setVisibility(View.GONE);
        mBeaconsAdapter.removeAll();
        mTimer.start();
        super.startScan();
    }

    @Override
    public void stopScan() {
        mProgressBar.setVisibility(View.GONE);
        super.stopScan();
        emptyListSetup();
    }

    @Override
    public void onCanScan() {
        emptyListSetup();
    }

    @Override
    public void updateBeaconList(final Collection<Beacon> beacons, final org.altbeacon.beacon.Region region) {
        if (getActivity() != null) {
            //update list, even nothing, we want update last seen time on detected beacons
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if(getActivity() == null)
                        return;

                    mBeaconsAdapter.notifyDataSetChanged();
                    Log.d(Constants.TAG, "called on region " + region.toString());
                }
            });
        }
    }

    @Override
    public void updateBeaconList(final Collection<Beacon> beacons) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if(getActivity() == null)
                        return;

                    mBeaconsAdapter.insertBeacons(beacons);
                    mBeaconsAdapter.sort(PreferencesUtil.getScanBeaconSort(getApplicationContext()));
                    mTimer.cancel();
                }
            });
        }
    }


    @Override
    public void onBeaconLongClick(int position) {
        Log.d("q","qqqq");
        mListBeacons.openContextMenu(position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo
            menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_detected_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuRecyclerView.RecyclerContextMenuInfo info = (ContextMenuRecyclerView
                .RecyclerContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId()) {
            case R.id.action_manage_add:

               // Log.d("god",Integer.toString(info.position));
                //find better way to change fragment from scan to tracked
               // Intent intent = MainNavigationActivity.getStartIntent(getActivity());
               // intent.putExtra(Constants.ARG_BEACON, new TrackedBeacon((IManagedBeacon) mBeaconsAdapter.getItem(info.position)));
               // startActivity(intent);
                return true;
            case R.id.action_filter_add:
                //TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void insertBeacons(Collection<Beacon> beacons) {
        Iterator<Beacon> iterator = beacons.iterator();
        while (iterator.hasNext()) {
            DetectedBeacon dBeacon = new DetectedBeacon(iterator.next());

           // Log.d("nicks",dBeacon.getEddystoneURL());

            dBeacon.setTimeLastSeen(System.currentTimeMillis());


        }

    }


}