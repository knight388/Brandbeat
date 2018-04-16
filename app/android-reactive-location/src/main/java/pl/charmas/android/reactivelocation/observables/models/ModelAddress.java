package pl.charmas.android.reactivelocation.observables.models;

import android.support.annotation.Nullable;

/**
 * Created by S.A.B. on 21.11.2015.
 */
public class ModelAddress {
    private String cityLong;
    private String cityShort;
    private String localityLong;
    private String localityShort;
    private String addressLong;
    private String addressShort;

    public String getCityLong() {
        return cityLong;
    }

    public ModelAddress setCityLong(String cityLong) {
        this.cityLong = cityLong;
        return this;
    }

    public String getCityShort() {
        return cityShort;
    }

    public ModelAddress setCityShort(String cityShort) {
        this.cityShort = cityShort;
        return this;
    }

    public String getLocalityLong() {
        return localityLong;
    }

    public ModelAddress setLocalityLong(String localityLong) {
        this.localityLong = localityLong;
        return this;
    }

    public String getLocalityShort() {
        return localityShort;
    }

    public ModelAddress setLocalityShort(String localityShort) {
        this.localityShort = localityShort;
        return this;
    }

    public String getAddressLong() {
        return addressLong;
    }

    public ModelAddress setAddressLong(String addressLong) {
        this.addressLong = addressLong;
        return this;
    }

    public String getAddressShort() {
        return addressShort;
    }

    public ModelAddress setAddressShort(String addressShort) {
        this.addressShort = addressShort;
        return this;
    }

    @Nullable
    public String getFirstNonNullLocalityName() {
        if (localityShort != null) {
            return localityShort;
        } else {
            return cityShort;
        }
    }

    @Override
    public String toString() {
        return "ModelAddress{" +
                "cityLong='" + cityLong + '\'' +
                ", cityShort='" + cityShort + '\'' +
                ", localityLong='" + localityLong + '\'' +
                ", localityShort='" + localityShort + '\'' +
                ", addressLong='" + addressLong + '\'' +
                ", addressShort='" + addressShort + '\'' +
                '}';
    }
}
