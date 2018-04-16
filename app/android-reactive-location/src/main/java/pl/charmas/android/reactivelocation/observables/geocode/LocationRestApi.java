package pl.charmas.android.reactivelocation.observables.geocode;

import pl.charmas.android.reactivelocation.observables.models.GoogleAddress;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by S.A.B. on 21.11.2015.
 */
public interface LocationRestApi{
    @GET("/geocode/json")
    GoogleAddress getAddress(@Query("latlng") String latlng, @Query("key") String key);
}
