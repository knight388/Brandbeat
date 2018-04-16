package com.mgrmobi.brandbeat.location.bean;

import android.location.Address;
import android.location.Location;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mgrmobi.brandbeat.entity.BrandBeatAddress;

import java.util.List;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface LocationBean {
    Observable<Location> getCurrentLocation();

    Observable<BrandBeatAddress> getCurrentAddress();

    Observable<AutocompletePredictionBuffer> getAutocompletePredictions(String query, LatLngBounds bounds, AutocompleteFilter filter);

    Observable<Place> getPlaceById(String placeId);

    Observable<Address> getAddressByLocation(LatLng location);

    Observable<Address> getAddressByName(String locationName);

    boolean isGpsEnabled();

    Observable<List<Address>> getListAddress(String name);
}