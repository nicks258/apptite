

package com.ateam.funshoppers.util;


public final class Constants {

    public static final String TAG = "BeaconLocator";
    public static final String DEFAULT_PROJECT_NAME = "default";
    public static final int SORT_DISTANCE_FAR_FIRST = 1;
    public static final int SORT_DISTANCE_NEAREST_FIRST = 0;
    public static final int SORT_UUID_MAJOR_MINOR = 2;
    public static final String TAG_FRAGMENT_SCAN_LIST = "SCAN_LIST";
    public static final String TAG_FRAGMENT_SCAN_RADAR = "SCAN_RADAR";
    public static final String TAG_FRAGMENT_MAIN = "MAIN";
    public static final String TAG_FRAGMENT_TRACKED_BEACON_LIST = "TRACKED_BEACON_LIST";

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_BEACON = "ARG_BEACON";
    public static final String ARG_ACTION_BEACON = "ARG_ACTION_BEACON";

    public static final int REQ_GLOBAL_SETTING = 10078;
    public static final int REQ_UPDATED_ACTION_BEACON = 10079;
    public static final int REQ_UPDATED_TRACKED_BEACON = 10080;
    public static final int REQ_TASKER_ACTION_NAME_REQUEST = 10081;
    public static final int REQ_CODE_ALERT_RINGTONE = 10082;

    public static final String REGION_NAME_PREFIX = "com.ateam.funshoppers";
    //actions
    public static final String NOTIFY_BEACON_ENTERS_REGION = "com.ateam.funshoppers.action.NOTIFY_BEACON_ENTERS_REGION";
    public static final String NOTIFY_BEACON_LEAVES_REGION = "com.ateam.funshoppers.action.NOTIFY_BEACON_LEAVES_REGION";
    public static final String NOTIFY_BEACON_NEAR_YOU_REGION = "com.ateam.funshoppers.action.NOTIFY_BEACON_NEAR_YOU_REGION";

    public static final String ALARM_NOTIFICATION_SHOW = "com.ateam.funshoppers.action.ALARM_NOTIFICATION_SHOW";
    public static final String GET_CURRENT_LOCATION = "com.ateam.funshoppers.action.GET_CURRENT_LOCATION";

    private Constants() {
    }


}
