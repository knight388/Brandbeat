package com.mgrmobi.brandbeat.entity;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class BrandBeatAddress {
    private String cityLong;
    private String cityShort;
    private String localityLong;
    private String localityShort;
    private String addressLong;
    private String addressShort;

    public String getCityLong() {
        return cityLong;
    }

    public BrandBeatAddress setCityLong(String cityLong) {
        this.cityLong = cityLong;
        return this;
    }

    public String getCityShort() {
        return cityShort;
    }

    public BrandBeatAddress setCityShort(String cityShort) {
        this.cityShort = cityShort;
        return this;
    }

    public String getLocalityLong() {
        return localityLong;
    }

    public BrandBeatAddress setLocalityLong(String localityLong) {
        this.localityLong = localityLong;
        return this;
    }

    public String getLocalityShort() {
        return localityShort;
    }

    public BrandBeatAddress setLocalityShort(String localityShort) {
        this.localityShort = localityShort;
        return this;
    }

    public String getAddressLong() {
        return addressLong;
    }

    public BrandBeatAddress setAddressLong(String addressLong) {
        this.addressLong = addressLong;
        return this;
    }

    public String getAddressShort() {
        return addressShort;
    }

    public BrandBeatAddress setAddressShort(String addressShort) {
        this.addressShort = addressShort;
        return this;
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
