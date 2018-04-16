package pl.charmas.android.reactivelocation;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Serega on 17.09.2015.
 */
public class RxLocationUtils {

    public static Location createDummyLocation(LatLng position){
        Location dummy = new Location(LocationManager.GPS_PROVIDER);
        dummy.setLatitude(position.latitude);
        dummy.setLongitude(position.longitude);

        return dummy;
    }
}
