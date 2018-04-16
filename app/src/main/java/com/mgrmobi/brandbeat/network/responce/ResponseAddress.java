package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseAddress implements Serializable
{
    @SerializedName("country")
    private String country;

    @SerializedName("city")
    private String city;

    @SerializedName("lat")
    String lat;

    @SerializedName("lng")
    String lng;

    @SerializedName("countryCode")
    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(final String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(final String lng) {
        this.lng = lng;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getString()
    {
        String address = "";
        if(country != null)
            address = country + ", ";
        if(city != null)
            address += city;
        return address;
    }
}
