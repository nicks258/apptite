
package com.ateam.funshoppers.ui.fragment;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ateam.funshoppers.R;
import com.ateam.funshoppers.ui.view.RadarScanView;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ScanRadarFragment extends ScanFragment {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.radar)
    RadarScanView mRadar;

    @Bind(R.id.distance)
    TextView mDistView;

    SensorManager mSensorManager;
    Sensor accSensor;
    Sensor magnetSensor;

    public static ScanRadarFragment newInstance() {
        ScanRadarFragment assetsFragment = new ScanRadarFragment();
        return assetsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_scan_radar, container, false);
        ButterKnife.bind(this, fragmentView);

        setupToolbar();

        mRadar.setUseMetric(true);
        mRadar.setDistanceView(mDistView);

        return fragmentView;
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mRadar, accSensor);
        mSensorManager.unregisterListener(mRadar, magnetSensor);
        super.onPause();
    }

    @Override
    public void onStop() {
        mRadar.stopSweep();
        super.onStop();
    }

    @Override
    public void startScan() {
        super.startScan();
        mRadar.startSweep();
    }

    @Override
    public void stopScan() {
        super.stopScan();
        mRadar.stopSweep();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBeaconManager.isBound(this)) mBeaconManager.setBackgroundMode(false);
        mSensorManager.registerListener(mRadar, accSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mRadar, magnetSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // actionBar.setDisplayShowTitleEnabled(true);
            mToolbar.setSubtitle(R.string.title_fragment_radar_beacons);
        }
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onCanScan() {
        // startScan();
    }


    @Override
    public void updateBeaconList(final Collection<Beacon> beacons, final org.altbeacon.beacon.Region region) {
        //update list, even nothing
    }

    @Override
    public void updateBeaconList(final Collection<Beacon> beacons) {
        if (mRadar == null || beacons.size() == 0) {
            return;
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    mRadar.onDetectedBeacons(beacons);
                }
            });
        }
    }

}
