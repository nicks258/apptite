/*
 * Copyright (C) 2015 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ateam.funshoppers.BeaconDistance;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.pwittchen.reactivebeacons.library.rx2.Beacon;
import com.github.pwittchen.reactivebeacons.library.rx2.Proximity;
import com.github.pwittchen.reactivebeacons.library.rx2.ReactiveBeacons;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.ateam.funshoppers.R;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;




import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class BeaconDisctance extends AppCompatActivity implements  BeaconConsumer,RangeNotifier  {
  private static final boolean IS_AT_LEAST_ANDROID_M =
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
  private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;
  private static final String ITEM_FORMAT = "MAC: %s, RSSI: %d\ndistance: %.2fm, proximity: %s\n%s";
  private static final String BEACON_DISPLAY_FORMAT = "Distance: %sm \n Beacon:%s " ;

  private BeaconManager mBeaconManager;
  private String beaconName ="";
  private ReactiveBeacons reactiveBeacons;
  private Disposable subscription;
  private String TAG = "MainActivity";
  private int itemLayoutId = android.R.layout.simple_list_item_1;
  private NotificationManager notif;
  private ListView lvBeacons;
  private Notification notify;
  private Map<String, Beacon> beacons;
  private Distance myAdapter;
  private BeaconManager beaconManager;
  private int BeaconStatus;
  public Toolbar toolbar;
  private static final Pattern urlPattern = Pattern.compile(
          "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                  + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                  + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
          Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.beacon_distance);
    lvBeacons = (ListView) findViewById(R.id.lv_beacons);
    toolbar=(Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);


    reactiveBeacons = new ReactiveBeacons(this);
    beacons = new HashMap<>();
  }

  @Override
  protected void onResume() {
    super.onResume();
    beaconManager = BeaconManager.getInstanceForApplication(this);
    mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
    // Detect the URL frame:
    mBeaconManager.getBeaconParsers().add(new BeaconParser().
            setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
    mBeaconManager.bind(this);
//    if (!canObserveBeacons()) {
//      return;
//    }
//
//    startSubscription();
  }

  private void startSubscription() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      requestCoarseLocationPermission();
      return;
    }

    subscription = reactiveBeacons.observe()
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Beacon>() {
          @Override
          public void accept(@NonNull Beacon beacon) throws Exception {
            beacons.put(beacon.device.getAddress(), beacon);
//            refreshBeaconList();
          }
        });
  }

  private boolean canObserveBeacons() {
    if (!reactiveBeacons.isBleSupported()) {
      Toast.makeText(this, "BLE is not supported on this device", Toast.LENGTH_SHORT).show();
      return false;
    }

    if (!reactiveBeacons.isBluetoothEnabled()) {
      reactiveBeacons.requestBluetoothAccess(this);
      return false;
    } else if (!reactiveBeacons.isLocationEnabled(this)) {
      reactiveBeacons.requestLocationAccess(this);
      return false;
    } else if (!isFineOrCoarseLocationPermissionGranted() && IS_AT_LEAST_ANDROID_M) {
      requestCoarseLocationPermission();
      return false;
    }

    return true;
  }

//  private void refreshBeaconList() {
//    List<String> list = new ArrayList<>();
//
//    for (Beacon beacon : beacons.values()) {
//      list.add(getBeaconItemString(beacon));
//    }
//
//    int itemLayoutId = android.R.layout.simple_list_item_1;
//    lvBeacons.setAdapter(new ArrayAdapter<>(this, itemLayoutId, list));
//  }

  private String getBeaconItemString(Beacon beacon) {
    String mac = beacon.device.getAddress();
    int rssi = beacon.rssi;
    double distance = beacon.getDistance();
    Proximity proximity = beacon.getProximity();
    String name = beacon.device.getName();
    Log.i("Name ->" ,beaconName);
    return String.format(ITEM_FORMAT, mac, rssi, distance, proximity, beaconName);
  }


  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @android.support.annotation.NonNull String[] permissions,
                                         @android.support.annotation.NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    final boolean isCoarseLocation = requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION;
    final boolean permissionGranted = grantResults[0] == PERMISSION_GRANTED;

    if (isCoarseLocation && permissionGranted && subscription == null) {
      startSubscription();
    }
  }

  private void requestCoarseLocationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(new String[] { ACCESS_COARSE_LOCATION },
          PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
    }
  }

  private boolean isFineOrCoarseLocationPermissionGranted() {
    boolean isAndroidMOrHigher = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    boolean isFineLocationPermissionGranted = isGranted(ACCESS_FINE_LOCATION);
    boolean isCoarseLocationPermissionGranted = isGranted(ACCESS_COARSE_LOCATION);

    return isAndroidMOrHigher && (isFineLocationPermissionGranted
        || isCoarseLocationPermissionGranted);
  }

  private boolean isGranted(String permission) {
    return ActivityCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED;
  }
  @Override
  public void onBeaconServiceConnect() {
    Region region = new Region("all-beacons-region", null, null, null);

    try {
      mBeaconManager.startRangingBeaconsInRegion(region);
    } catch (RemoteException e) {
      com.orhanobut.logger.Logger.i("approximately + catch");
      e.printStackTrace();
    }
    mBeaconManager.setRangeNotifier(this);
    beaconManager.addMonitorNotifier(new MonitorNotifier() {
      @Override
      public void didEnterRegion(Region region) {
        Log.i(TAG, "I just saw an beacon for the first time!");
      }

      @Override
      public void didExitRegion(Region region) {
        Log.i(TAG, "I no longer see an beacon");
      }

      @Override
      public void didDetermineStateForRegion(int state, Region region) {
        BeaconStatus = state;
        Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
      }
    });

    try {
      beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
    } catch (RemoteException e) {    }
  }




//  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//  @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
//  @Override
//  public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//    final List<String> list = new ArrayList<>();
//    String Intenturl="";
//    String item ="";
//    for (org.altbeacon.beacon.Beacon beacon : beacons) {
//      if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10) {
//        String url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
//        String distance = " "+beacon.getDistance();
//        com.orhanobut.logger.Logger.i(""+beacon.getDistance()+ " Collection Size"+ beacons.size());
//        Log.d(TAG, "I see a beacon transmitting a url: " + url +
//                " approximately " + beacon.getDistance() + " meters away.");
//        int matchStart=0,matchEnd=0;
//        Matcher matcher = urlPattern.matcher(url);
//        while (matcher.find()) {
//          matchStart = matcher.start(1);
//          matchEnd = matcher.end();
//          com.orhanobut.logger.Logger.i("Position"+ matchStart + " "  +matchEnd);
//          Intenturl = url.substring(matchStart,matchEnd);
//          // now you have the offsets of a URL match
//        }
//        String keyword = Intenturl;
//        keyword = keyword.substring(keyword.lastIndexOf("/")+1);
//
//        notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        notify=new Notification.Builder
//                (getApplicationContext()).setContentTitle(keyword).setContentText("Beacons Around You").
//                setContentTitle(keyword).setSmallIcon(R.mipmap.shopcolo).build();
//        notify.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        item = String.format(BEACON_DISPLAY_FORMAT,distance.substring(0,6), keyword);
//        com.orhanobut.logger.Logger.i("approximately" + keyword);
//        final String finalItem = item;
//        runOnUiThread(new Runnable() {
//          @Override
//          public void run() {
//            list.add(finalItem);
//            for (int i=0;i<list.size();i++) {
//              notif.notify(i, notify);
//            }
//            Collections.sort(list);
//            myAdapter=new Distance(getApplicationContext(),R.layout.item_layout,list);
//            lvBeacons.setAdapter(myAdapter);
////            lvBeacons.setOnItemClickListener( new AdapterView.OnItemClickListener() {
////
////              @Override
////              public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
////                com.orhanobut.logger.Logger.i("Hello Sumit");
////                int matchStart=0,matchEnd=0;
////                String Intenturl="";
////                String data=(String)arg0.getItemAtPosition(arg2);
////                com.orhanobut.logger.Logger.i("Position->>Data"+data);
////                String Rese ="http://" + data.substring(7) ;
////                Log.i("Nick",Rese);
////                Matcher matcher = urlPattern.matcher(Rese);
////                while (matcher.find()) {
////                  matchStart = matcher.start(1);
////                  matchEnd = matcher.end();
////                  com.orhanobut.logger.Logger.i("Position"+ matchStart + " "  +matchEnd);
////                  Intenturl = data.substring(matchStart,matchEnd);
////                  // now you have the offsets of a URL match
////                }
////
////                Log.i("Nick->>",Intenturl);
////                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
////                Log.i("opo",Intenturl.substring(7,Intenturl.lastIndexOf(".")));
////                intent.putExtra("url",Intenturl.substring(7,Intenturl.lastIndexOf(".")));
////                startActivity(intent);
////              }
////            });
//
//
//          }
//        });
//
//
//      }
//
//
//    }
//    com.orhanobut.logger.Logger.i("BEACONSTATUS" + BeaconStatus );
//    com.orhanobut.logger.Logger.i(canObserveBeacons()+"approximately + endoffor");
//    if (BeaconStatus==0)
//    {
//      com.orhanobut.logger.Logger.i(canObserveBeacons()+"approximately + endif");
//      list.clear();
//      runOnUiThread(new Runnable() {
//        @Override
//        public void run() {
//
//          lvBeacons.setAdapter(new ArrayAdapter<>(MainActivity.this, itemLayoutId, list));
//
//        }
//      });
//
//
//    }
//
////    if (mBeaconManager.checkAvailability()) {
//////    list.clear();
////      com.orhanobut.logger.Logger.i("approximately + false");
////    }
////    lvBeacons.setAdapter(new ArrayAdapter<>(this, itemLayoutId, list));
////    for (org.altbeacon.beacon.Beacon beacon : beacons) {
////      if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10) {
////        // This is a Eddystone-URL frame
////        String url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
//////                String beaconName = beacon.getBluetoothName();
////
////
//////        Logger.i("yoooo"+modalClass.getID());
//////        Logger.i(beaconName);
////// TODO AUTOMATIC FUNCTION
////// runOnUiThread(new Runnable() {
//////          @Override
//////          public void run() {
////////            beaconTv = (TextView) findViewById(R.id.beacon);
////////            beaconTv.setText(beaconName);
//////          }
//////        });
////
////        String beaconDetail = beacon.getParserIdentifier()+ " " + beacons.toString() + " " +
////                beacon.describeContents() + " " + beacon.getServiceUuid()+ " " + beacon.getId1() ;
//////        Logger.i(beaconDetail);
//
////        Identifier namespaceId = beacon.getId1();
////        beaconName = url;
//////                Identifier instanceId = beacon.getId2();
////        Log.d(TAG, "I see a beacon transmitting namespace id: "+namespaceId+
////                " and instance id: "+
////                " approximately "+beacon.getDistance()+" meters away.");
////      }
////    }
//  }



  @Override
  public void onPause() {
    super.onPause();
    mBeaconManager.unbind(this);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
  @Override
  public void didRangeBeaconsInRegion(Collection<org.altbeacon.beacon.Beacon> beacons, Region region) {
    final List<String> list = new ArrayList<>();
    String Intenturl="";
    String item ="";
    for (org.altbeacon.beacon.Beacon beacon : beacons) {
      if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10) {
        String url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
        String distance = " "+beacon.getDistance();
        com.orhanobut.logger.Logger.i(""+beacon.getDistance()+ " Collection Size"+ beacons.size());
        Log.d(TAG, "I see a beacon transmitting a url: " + url +
                " approximately " + beacon.getDistance() + " meters away.");
        int matchStart=0,matchEnd=0;
        Matcher matcher = urlPattern.matcher(url);
        while (matcher.find()) {
          matchStart = matcher.start(1);
          matchEnd = matcher.end();
          com.orhanobut.logger.Logger.i("Position"+ matchStart + " "  +matchEnd);
          Intenturl = url.substring(matchStart,matchEnd);
          // now you have the offsets of a URL match
        }
        String keyword = Intenturl;
        keyword = keyword.substring(keyword.lastIndexOf("/")+1);

        notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notify=new Notification.Builder
                (getApplicationContext()).setContentTitle(keyword).setContentText("Beacons Around You").
                setContentTitle(keyword).setSmallIcon(R.mipmap.shopcolo).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;

        item = String.format(BEACON_DISPLAY_FORMAT,distance.substring(0,6), keyword);
        com.orhanobut.logger.Logger.i("approximately" + keyword);
        final String finalItem = item;
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            list.add(finalItem);
            for (int i=0;i<list.size();i++) {
              notif.notify(i, notify);
            }
            Collections.sort(list);
            myAdapter=new Distance(getApplicationContext(),R.layout.item_layout,list);
            lvBeacons.setAdapter(myAdapter);
//            lvBeacons.setOnItemClickListener( new AdapterView.OnItemClickListener() {
//
//              @Override
//              public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//                com.orhanobut.logger.Logger.i("Hello Sumit");
//                int matchStart=0,matchEnd=0;
//                String Intenturl="";
//                String data=(String)arg0.getItemAtPosition(arg2);
//                com.orhanobut.logger.Logger.i("Position->>Data"+data);
//                String Rese ="http://" + data.substring(7) ;
//                Log.i("Nick",Rese);
//                Matcher matcher = urlPattern.matcher(Rese);
//                while (matcher.find()) {
//                  matchStart = matcher.start(1);
//                  matchEnd = matcher.end();
//                  com.orhanobut.logger.Logger.i("Position"+ matchStart + " "  +matchEnd);
//                  Intenturl = data.substring(matchStart,matchEnd);
//                  // now you have the offsets of a URL match
//                }
//
//                Log.i("Nick->>",Intenturl);
//                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
//                Log.i("opo",Intenturl.substring(7,Intenturl.lastIndexOf(".")));
//                intent.putExtra("url",Intenturl.substring(7,Intenturl.lastIndexOf(".")));
//                startActivity(intent);
//              }
//            });


          }
        });


      }


    }
//    com.orhanobut.logger.Logger.i("BEACONSTATUS" + BeaconStatus );
    com.orhanobut.logger.Logger.i(canObserveBeacons()+"approximately + endoffor");
    if (BeaconStatus==0)
    {

      com.orhanobut.logger.Logger.i(canObserveBeacons()+"approximately + endif");
      list.clear();
      runOnUiThread(new Runnable() {
        @Override
        public void run() {

          lvBeacons.setAdapter(new ArrayAdapter<>(BeaconDisctance.this, itemLayoutId, list));

        }
      });


    }

//    if (mBeaconManager.checkAvailability()) {
////    list.clear();
//      com.orhanobut.logger.Logger.i("approximately + false");
//    }
//    lvBeacons.setAdapter(new ArrayAdapter<>(this, itemLayoutId, list));
//    for (org.altbeacon.beacon.Beacon beacon : beacons) {
//      if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10) {
//        // This is a Eddystone-URL frame
//        String url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
////                String beaconName = beacon.getBluetoothName();
//
//
////        Logger.i("yoooo"+modalClass.getID());
////        Logger.i(beaconName);
//// TODO AUTOMATIC FUNCTION
//// runOnUiThread(new Runnable() {
////          @Override
////          public void run() {
//////            beaconTv = (TextView) findViewById(R.id.beacon);
//////            beaconTv.setText(beaconName);
////          }
////        });
//
//        String beaconDetail = beacon.getParserIdentifier()+ " " + beacons.toString() + " " +
//                beacon.describeContents() + " " + beacon.getServiceUuid()+ " " + beacon.getId1() ;
////        Logger.i(beaconDetail);

//        Identifier namespaceId = beacon.getId1();
//        beaconName = url;
////                Identifier instanceId = beacon.getId2();
//        Log.d(TAG, "I see a beacon transmitting namespace id: "+namespaceId+
//                " and instance id: "+
//                " approximately "+beacon.getDistance()+" meters away.");
//      }
//    }
  }
}
