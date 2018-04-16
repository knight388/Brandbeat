package pl.charmas.android.reactivelocation.observables.models;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by S.A.B. on 21.11.2015.
 */
public class MapperGooglePlaceToAddress {
    private static final String TYPE_CITY = "administrative_area_level_2";  //City
    private static final String TYPE_LOCALITY = "sublocality_level_2"; //Tsentralnyy Raion
    private static final String TYPE_ROUTE = "route";   //Street

    static class ResultHolder {
        GoogleAddressComponent city;
        GoogleAddressComponent locality;
        GoogleAddressComponent route;

        boolean hasAllData() {
            return city != null && locality != null && route != null;
        }
    }

    @Nullable
    public ModelAddress mapTo(GoogleAddress entity) {
        if (entity.getItems() == null || entity.getItems().isEmpty()) {
            return null;
        }

        List<GoogleAddress.Item> data = entity.getItems();
        ResultHolder info = findComponent(data);

        GoogleAddressComponent city = info.city;
        GoogleAddressComponent locality = info.locality;
        GoogleAddressComponent route = info.route;

        ModelAddress address = new ModelAddress();
        if (city != null) {
            address.setCityShort(city.getShortName()).setCityLong(city.getLongName());
        }
        if (locality != null) {
            address.setLocalityShort(locality.getShortName()).setLocalityLong(locality.getLongName());
        }
        if (route != null) {
            address.setAddressShort(route.getShortName()).setAddressLong(route.getLongName());
        }
        return address;
    }

    private static ResultHolder findComponent(List<GoogleAddress.Item> data) {
        ResultHolder result = new ResultHolder();
        for (GoogleAddress.Item p : data) {
            for (GoogleAddressComponent c : p.getComponents()) {
                if (c.getTypes().contains(TYPE_CITY)) {
                    result.city = c;
                } else if (c.getTypes().contains(TYPE_LOCALITY)) {
                    result.locality = c;
                } else if (c.getTypes().contains(TYPE_ROUTE)) {
                    result.route = c;
                }
                if (result.hasAllData()) {
                    return result;
                }
            }
        }
        return result;
    }
}
