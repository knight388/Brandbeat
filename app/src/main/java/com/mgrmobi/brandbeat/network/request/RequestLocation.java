package com.mgrmobi.brandbeat.network.request;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestLocation implements Serializable {
    @SerializedName("city")
    private String city;

    @SerializedName("country")
    private String country;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("countryCode")
    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public void setLocation(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(final double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(final double lng) {
        this.lng = lng;
    }

    public String getAddressString() {
        String address = "";
        if(country != null)
        {
            address = country;
        }
        if(city != null)
        {
            if(address.length() > 0)
            {
                address += " " + city;
            }
            else
            {
                address = city;
            }
        }
        return address;
    }
}
