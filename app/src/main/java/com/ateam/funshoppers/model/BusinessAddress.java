package com.ateam.funshoppers.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 12/12/2016.
 */

public class BusinessAddress {
    @SerializedName("Id")

    private Integer id;
    @SerializedName("Line1")

    private String line1;
    @SerializedName("Location")

    private Location location;
    @SerializedName("Latitude")

    private String latitude;
    @SerializedName("Longitude")

    private String longitude;
    @SerializedName("Pincode")
    private String pincode;
    @SerializedName("Neighbourhood")

    private String neighbourhood;
    @SerializedName("City")

    private String city;
    @SerializedName("State")

    private String state;
    @SerializedName("Country")
    private String country;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The Id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The line1
     */
    public String getLine1() {
        return line1;
    }

    /**
     *
     * @param line1
     * The Line1
     */
    public void setLine1(String line1) {
        this.line1 = line1;
    }

    /**
     *
     * @return
     * The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The Location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The Latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The Longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The pincode
     */
    public String getPincode() {
        return pincode;
    }

    /**
     *
     * @param pincode
     * The Pincode
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    /**
     *
     * @return
     * The neighbourhood
     */
    public String getNeighbourhood() {
        return neighbourhood;
    }

    /**
     *
     * @param neighbourhood
     * The Neighbourhood
     */
    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    /**
     *
     * @return
     * The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The City
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The state
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     * The State
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     * The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The Country
     */
    public void setCountry(String country) {
        this.country = country;
    }

}


 class Geography {

    @SerializedName("CoordinateSystemId")
    private Integer coordinateSystemId;
    @SerializedName("WellKnownText")
    private String wellKnownText;

    /**
     *
     * @return
     * The coordinateSystemId
     */
    public Integer getCoordinateSystemId() {
        return coordinateSystemId;
    }

    /**
     *
     * @param coordinateSystemId
     * The CoordinateSystemId
     */
    public void setCoordinateSystemId(Integer coordinateSystemId) {
        this.coordinateSystemId = coordinateSystemId;
    }

    /**
     *
     * @return
     * The wellKnownText
     */
    public String getWellKnownText() {
        return wellKnownText;
    }

    /**
     *
     * @param wellKnownText
     * The WellKnownText
     */
    public void setWellKnownText(String wellKnownText) {
        this.wellKnownText = wellKnownText;
    }

}
class Location {

    @SerializedName("Geography")
    private Geography geography;

    /**
     *
     * @return
     * The geography
     */
    public Geography getGeography() {
        return geography;
    }

    /**
     *
     * @param geography
     * The Geography
     */
    public void setGeography(Geography geography) {
        this.geography = geography;
    }
}
