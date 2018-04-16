package pl.charmas.android.reactivelocation.observables.geocode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.gson.Gson;
import pl.charmas.android.reactivelocation.BuildConfig;
import pl.charmas.android.reactivelocation.observables.models.GoogleAddress;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Subscriber;

import java.io.IOException;

/**
 * Created by S.A.B. on 21.11.2015.
 */
public class ReverseGeocodeGoogleApiObservable implements Observable.OnSubscribe<GoogleAddress> {
    private final Context ctx;
    private final double latitude;
    private final double longitude;
    private final String googleApiKey;
    private final LocationRestApi restApi;

    public static Observable<GoogleAddress> createObservable(Context ctx, double latitude, double longitude, String googleApiKey) {
        return Observable.create(new ReverseGeocodeGoogleApiObservable(ctx, latitude, longitude, googleApiKey));
    }

    private ReverseGeocodeGoogleApiObservable(Context ctx, double latitude, double longitude, String googleApiKey) {
        this.ctx = ctx;
        this.latitude = latitude;
        this.longitude = longitude;
        this.googleApiKey = googleApiKey;

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com/maps/api")
                .setConverter(new GsonConverter(new Gson()));

        if (BuildConfig.DEBUG) {
            builder.setLog(new AndroidLog("LOCATION_LOG")).setLogLevel(RestAdapter.LogLevel.FULL);
        }

        restApi = builder.build().create(LocationRestApi.class);
    }

    @Override
    public void call(Subscriber<? super GoogleAddress> subscriber) {
        if(subscriber.isUnsubscribed()){
            return;
        }
        if (!isInternetConnectionAvailable(ctx)) {
            subscriber.onError(RetrofitError.networkError("", new IOException("Network not available")));
            subscriber.onCompleted();
            return;
        }
        try {
            GoogleAddress result = restApi.getAddress(latitude + "," + longitude, googleApiKey);
            if(!subscriber.isUnsubscribed()) {
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        } catch (RetrofitError error) {
            if(!subscriber.isUnsubscribed()) {
                subscriber.onError(error);
                subscriber.onCompleted();
            }
        }
    }

    private static boolean isInternetConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}
