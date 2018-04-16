package pl.charmas.android.reactivelocation.observables.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by S.A.B. on 21.11.2015.
 */
public class GoogleAddress {
    @SerializedName("results") private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public static final class Item {
        @SerializedName("address_components") private List<GoogleAddressComponent> components;

        public List<GoogleAddressComponent> getComponents() {
            return components;
        }
    }
}
