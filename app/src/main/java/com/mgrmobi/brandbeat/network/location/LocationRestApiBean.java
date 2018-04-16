package com.mgrmobi.brandbeat.network.location;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.AbstractRestApi;

import pl.charmas.android.reactivelocation.observables.geocode.LocationRestApi;
import pl.charmas.android.reactivelocation.observables.models.GoogleAddress;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class LocationRestApiBean  extends AbstractRestApi {
    private final LocationRestApi locationRestApi;

    public LocationRestApiBean(Context appContext) {
        super(appContext);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com/maps/api")
                .setLog(new AndroidLog("LOCATION_LOG"))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(new Gson()))
                .build();

        locationRestApi = restAdapter.create(LocationRestApi.class);
    }

    public Observable<GoogleAddress> getAddressByCoordinates(@NonNull final Location location) {

        return Observable.<GoogleAddress>create(subscriber -> {
            try {
                GoogleAddress result = locationRestApi.getAddress(location.getLatitude() + "," + location.getLongitude(),
                        appContext.getString(R.string.google_places_api_key));
                subscriber.onNext(result);
            } catch (RetrofitError error) {
                subscriber.onError(error);
            }
            subscriber.onCompleted();
        });
    }
}
