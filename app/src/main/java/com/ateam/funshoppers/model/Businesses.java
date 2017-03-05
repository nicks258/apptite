package com.ateam.funshoppers.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 11/30/2016.
 */

public class Businesses {



    @SerializedName("Appointments")
    private boolean Appointments;
    @SerializedName("Customizations")
    private boolean Customizations;
    @SerializedName("OnlineOrders")
    private boolean OnlineOrders;
    @SerializedName("Id")
    private String Id;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Description")
    private String Description;
    @SerializedName("Highlights")
    private String Highlights;
    @SerializedName("Type")
    private String Type;
    @SerializedName("IsVerified")
    private boolean IsVerified;
    @SerializedName("Rating")
    private Double Rating;
    @SerializedName("Categories")
    private ArrayList<Integer> Categories;
    @SerializedName("CoverPhotoUrl")
    private String CoverPhotoUrl;

    public String getName() {
        return Name;
    }

    public String getCoverPhotoUrl() {
        return CoverPhotoUrl;
    }

    public String getId() {
        return Id;
    }

    public String getHighlights() {
        return Highlights;
    }

    public boolean isVerified() {
        return IsVerified;
    }

    public boolean isAppointments() {
        return Appointments;
    }

    public boolean isCustomizations() {
        return Customizations;
    }

    public boolean isOnlineOrders() {
        return OnlineOrders;
    }
    public Double getRating() {
        return Rating;
    }

    public ArrayList<Integer> getCategories() {
        return Categories;
    }



    public String getType() {
        return Type;
    }

    public String getDescription() {
        return Description;
    }


    public class DeliveryMethod{
        @SerializedName("Pickup")
        private boolean Pickup;
        @SerializedName("Delivery")
        private boolean Delivery;

        public boolean isPickup() {
            return Pickup;
        }

        public boolean isDelivery() {
            return Delivery;
        }
    }
    public class PaymentMode{
        @SerializedName("Cash")
        private boolean Cash;
        @SerializedName("CreditCard")
        private boolean CreditCard;

        public boolean isCash() {
            return Cash;
        }

        public boolean isCreditCard() {
            return CreditCard;
        }
    }
    public class category{
        private int category;

        public int getCategory() {
            return category;
        }
    }
}
