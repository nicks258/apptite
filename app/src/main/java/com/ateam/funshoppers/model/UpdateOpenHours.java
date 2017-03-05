package com.ateam.funshoppers.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Home on 12/2/2016.
 */

public class UpdateOpenHours {
    ArrayList<day> openHours;

    public UpdateOpenHours(ArrayList<day> openHours) {
        this.openHours = openHours;
    }

    public static class day{
        public day(String weekday, String starttime, String endtime) {
            Weekday = weekday;
            Starttime = starttime;
            Endtime = endtime;
        }

        private String Weekday;
        private String Starttime;
        private String Endtime;

        public String getWeekday() {
            return Weekday;
        }

        public String getStarttime() {
            return Starttime;
        }

        public String getEndtime() {
            return Endtime;
        }
    }
}
