package com.ateam.funshoppers.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 11/30/2016.
 */

public class Business {
    public String getName() {
        return Name;
    }

    public String getCoverPhotoUrl() {
        return CoverPhotoUrl;
    }

    @SerializedName("Name")
    private String Name;
    @SerializedName("Appointments")
    private boolean Appointments;
    @SerializedName("Customizations")
    private boolean Customizations;
    @SerializedName("IsVerified")
    private boolean IsVerified;
    @SerializedName("OnlineOrders")
    private boolean OnlineOrders;
    @SerializedName("DeliveryMethod")
    private DeliveryMethod deliveryMethods;
    @SerializedName("PaymentMode")
    private PaymentMode paymentMode;
    @SerializedName("ExternalLinks")
    private ExternalLinks ExternalLinks;
    @SerializedName("Rating")
    private Integer Rating;
    @SerializedName("Categories")
    private ArrayList<Integer> Categories;
    @SerializedName("CoverPhotoUrl")
    private String CoverPhotoUrl;
    @SerializedName("Description")
    private String Description;
    @SerializedName("Type")
    private String Type;
    @SerializedName("OpenHours")
    private ArrayList<UpdateOpenHours.day> OpenHours;

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

    public DeliveryMethod getDeliveryMethods() {
        return deliveryMethods;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public ExternalLinks getExternalLinks() {
        return ExternalLinks;
    }

    public Integer getRating() {
        return Rating;
    }

    public ArrayList<Integer> getCategories() {
        return Categories;
    }



    public ArrayList<UpdateOpenHours.day> getOpenHours() {
        return OpenHours;
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
