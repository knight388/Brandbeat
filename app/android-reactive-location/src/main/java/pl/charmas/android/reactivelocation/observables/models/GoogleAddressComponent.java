package pl.charmas.android.reactivelocation.observables.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by S.A.B. on 21.11.2015.
 */
public class GoogleAddressComponent {
    @SerializedName("long_name") private String longName;
    @SerializedName("short_name") private String shortName;
    @SerializedName("types") private List<String> types;

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public List<String> getTypes() {
        return types;
    }
}
