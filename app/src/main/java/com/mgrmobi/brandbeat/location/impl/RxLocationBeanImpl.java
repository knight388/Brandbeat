package com.mgrmobi.brandbeat.location.impl;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.data.AbstractDataBuffer;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mgrmobi.brandbeat.entity.BrandBeatAddress;
import com.mgrmobi.brandbeat.entity.mappers.MapperGooglePlaceToAdadress;
import com.mgrmobi.brandbeat.location.bean.LocationBean;
import com.mgrmobi.brandbeat.network.location.LocationRestApiBean;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@Singleton
public class RxLocationBeanImpl implements LocationBean {
    private final ReactiveLocationProvider locationProvider;
    private final LocationManager locationManager;
    private final LocationRestApiBean locationRestApi;
    private final MapperGooglePlaceToAdadress mapper;

    @Inject
    public RxLocationBeanImpl(Context appContext) {
        locationProvider = new ReactiveLocationProvider(appContext);
        locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
        locationRestApi = new LocationRestApiBean(appContext);
        mapper = new MapperGooglePlaceToAdadress();
    }

    @Override
    public Observable<Location> getCurrentLocation() {

        return locationProvider.getLastKnownLocation()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BrandBeatAddress> getCurrentAddress() {

        Observable<Location> currentLocation = getCurrentLocation().subscribeOn(Schedulers.io());
        return currentLocation
                .flatMap((loc) -> locationRestApi.getAddressByCoordinates(loc).subscribeOn(Schedulers.io()))
                .map(mapper::mapTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<AutocompletePredictionBuffer> getAutocompletePredictions(String query, LatLngBounds bounds, AutocompleteFilter filter) {
        return locationProvider.getPlaceAutocompletePredictions(query, bounds, filter)
                .filter(RxLocationBeanImpl::checkBuffer)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Place> getPlaceById(String placeId) {

        return locationProvider.getPlaceById(placeId)
                .filter(RxLocationBeanImpl::checkBuffer)
                .map(buffer -> {
                    Place place = buffer.get(0).freeze();
                    buffer.release();
                    return place;
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Address> getAddressByLocation(@Nullable LatLng location) {
        if (location != null) {
            return locationProvider.getReverseGeocodeObservable(location.latitude, location.longitude, 1)
                    .filter(list -> !list.isEmpty()).map(list -> list.get(0))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
        return locationProvider.getLastKnownLocation()
                .flatMap(loc -> locationProvider.getReverseGeocodeObservable(loc.getLatitude(), loc.getLongitude(), 1)
                        .filter(list -> !list.isEmpty()).map(list -> list.get(0)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Address> getAddressByName(String locationName) {
        return locationProvider.getGeocodeObservable(locationName, 1)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

    private static boolean checkBuffer(Result result) {
        if (!result.getStatus().isSuccess()) {
            ((AbstractDataBuffer) result).release();
            return false;
        }
        return true;
    }

    @Override
    public boolean isGpsEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public Observable<List<Address>> getListAddress(final String name) {
        return locationProvider.getGeocodeObservable(name, 5)
               // .filter(list -> !list.isEmpty())
                .map(list -> list);
    }
}
