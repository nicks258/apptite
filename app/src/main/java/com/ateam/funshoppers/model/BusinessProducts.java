package com.ateam.funshoppers.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 11/30/2016.
 */

public class BusinessProducts {
    @SerializedName("CoverPhotoUrl")
    private String coverphotourl;
    @SerializedName("Name")
    private String name;
    @SerializedName("Id")
    private String Id;
    @SerializedName("Description")
    private String description;
    @SerializedName("Customizations")
    private boolean Customizations;
    @SerializedName("Variants")
    private ArrayList<Variants> variants;
    @SerializedName("Rating")
    private int Rating;
    @SerializedName("isVerified")
    private boolean isVerified;

    public boolean isCustomizations() {
        return Customizations;
    }

    public ArrayList<Variants> getVariants() {
        return variants;
    }

    public int getRating() {
        return Rating;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public String getId() {
        return Id;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverphotourl() {
        return coverphotourl;
    }

    public String getName() {
        return name;
    }

    public class Variants{
        @SerializedName("PriceTypeOption")
        private String PriceTypeOption;
        @SerializedName("PriceStart")
        private int PriceStart;
        @SerializedName("PriceEnd")
        private int PriceEnd;
        @SerializedName("Quantity")
        private int Quantity;

        public String getPriceTypeOption() {
            return PriceTypeOption;
        }

        public int getPriceStart() {
            return PriceStart;
        }

        public int getPriceEnd() {
            return PriceEnd;
        }

        public int getQuantity() {
            return Quantity;
        }

        public void setPriceTypeOption(String priceTypeOption) {
            PriceTypeOption = priceTypeOption;
        }

        public void setPriceStart(int priceStart) {
            PriceStart = priceStart;
        }

        public void setPriceEnd(int priceEnd) {
            PriceEnd = priceEnd;
        }

        public void setQuantity(int quantity) {
            Quantity = quantity;
        }
    }
}
