package com.ateam.funshoppers.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Home on 11/30/2016.
 */

public class BusinessReviews {
@SerializedName("Body")
private String Body;
    @SerializedName("Id")
    private int id;
    @SerializedName("Rating")
    private Integer Rating;
    @SerializedName("CreatedBy")
    private UserDetails createdBy;
    @SerializedName("CreatedOn")
    private String createdOn;

    public String getModifiedOn() {
        return modifiedOn;
    }

    @SerializedName("ModifiedOn")
    private String modifiedOn;

    public String getCreatedOn() {
        return createdOn;
    }

    public String getBody() {
        return Body;
    }

    public int getId() {
        return id;
    }

    public double getRating() {
        return Rating/2.0;
    }

    public UserDetails getCreatedBy() {
        return createdBy;
    }

    public class UserDetails{
        @SerializedName("Id")
        private int UserId;
        @SerializedName("Email")
        private String Email;
        @SerializedName("FullName")
        private String FullName;

        public int getUserId() {
            return UserId;
        }

        public String getEmail() {
            return Email;
        }

        public String getFullName() {
            return FullName;
        }
    }


}
