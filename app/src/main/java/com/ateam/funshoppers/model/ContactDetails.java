package com.ateam.funshoppers.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 12/8/2016.
 */

public class ContactDetails {
    @SerializedName("PhoneNumber")
    private String PhoneNumber;
    @SerializedName("Email")
    private String Email;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getEmail() {
        return Email;
    }
}
