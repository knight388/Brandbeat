package com.mgrmobi.brandbeat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.Menu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgrmobi.brandbeat.network.request.RequestBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseLogin;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@Singleton
public class UserDataUtils {
    public static final String KEY_USER_ID = "com.mgrmobi.brandbeat.USER_ID";
    public static final String KEY_USER_PROFILE_TOKEN = "com.mgrmobi.brandbeat.TOKEN";
    public static final String KEY_USER_PROFILE_RERFRESH_TOKEN = "com.mgrmobi.brandbeat.REFRESH_TOKEN";
    public static final String USER_IMAGE = "com.mgrmobi.brandbeat.USER_IMAGE";
    public static final String USER_EMAIL = "com.mgrmobi.brandbeat.USER_EMAIL";
    public static final String USER_NAME = "com.mgrmobi.brandbeat.USER_NAME";
    public static final String RESPONSE_BRAND = "com.mgrmobi.brandbeat.RESPONSE_BRANDS";
    public static final String EXPIRED_DATA = "com.mgrmobi.brandbeat.EXPIRE_DATE";

    public static final String ADD_BRAND = "com.mgrmobi.brandbeat.ADDED_BRAND";
    public static final String BRAND_NAME = "com.mgrmobi.brandbeat.brand_name";
    public static final String BRAND_DESCRIPTION = "com.mgrmobi.brandbeat.brand_description";
    public static final String BRAND_CATEGORY = "com.mgrmobi.brandbeat.brand_category";
    public static final String BRAND_SUBCATEGORY = "com.mgrmobi.brandbeat.brand_subcategory";

    private final Context context;

    @Inject
    public UserDataUtils(Context context) {
        this.context = context;
    }

    public void saveUserProfile(ResponseProfile data) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_IMAGE, data.getPhotoSave())
                .putString(USER_EMAIL, data.getEmail())
                .putString(USER_NAME, data.getUsername())
                .apply();
    }

    public void saveUserData(ResponseLogin data) {
        if (data.getResponseAuthInfo() == null) {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putString(KEY_USER_PROFILE_TOKEN, data.getAccessToken())
                    .putString(KEY_USER_PROFILE_RERFRESH_TOKEN, data.getRefreshToken())
                    .putString(EXPIRED_DATA, data.getAccessTokenExpire())
                    .putString(KEY_USER_ID, data.getResponseAuthInfo().getId())
                    .apply();
        }
        else {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putString(KEY_USER_ID, data.getResponseAuthInfo().getId())
                    .putString(KEY_USER_PROFILE_TOKEN, data.getAccessToken())
                    .putString(KEY_USER_PROFILE_RERFRESH_TOKEN, data.getRefreshToken())
                    .putString(EXPIRED_DATA, data.getAccessTokenExpire())
                    .apply();
        }
    }

    public void saveUserDataRegistration(ResponseLogin data) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_USER_ID, data.getUserId())
                .putString(KEY_USER_PROFILE_TOKEN, data.getAccessToken())
                .putString(KEY_USER_PROFILE_RERFRESH_TOKEN, data.getRefreshToken())
                .putString(EXPIRED_DATA, data.getAccessTokenExpire())
                .apply();
    }

    public void saveData(String key, String data) {
        if(context == null) return;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, data).apply();
    }

    public ResponseProfile getUserInfo() {
        ResponseProfile responseProfile = new ResponseProfile();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        responseProfile.setUsername(prefs.getString(USER_NAME, ""));
        responseProfile.setEmail(prefs.getString(USER_EMAIL, ""));
        responseProfile.setPhoto(prefs.getString(USER_IMAGE, ""));
        return responseProfile;
    }

    public String getUserData(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, "");
    }

    public String getUserId() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_USER_ID, "");
    }

    public void clear() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .remove(KEY_USER_PROFILE_RERFRESH_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_PROFILE_TOKEN)
                .remove(USER_NAME)
                .remove(USER_EMAIL)
                .remove(USER_IMAGE)
                .apply();
    }

    public void saveComparisonView(ArrayList<ResponseBrand> responseBrands) {
        Gson gson = new Gson();
        String jsonArray = gson.toJson(responseBrands);
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(RESPONSE_BRAND, jsonArray)
                .apply();
    }

    public ArrayList<ResponseBrand> getBrandsToCompare() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String str = prefs.getString(RESPONSE_BRAND, "");
        Gson gson = new Gson();
        ArrayList<ResponseBrand> m = gson.fromJson(str, new TypeToken<ArrayList<ResponseBrand>>() {
        }.getType());
        ArrayList<ResponseBrand> responseBrands = new ArrayList<>();
        return m;
    }

    public void saveAddedBrand(RequestBrand requestBrand) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(BRAND_NAME, requestBrand.getTitle())
                .putString(BRAND_DESCRIPTION, requestBrand.getText())
                .putString(BRAND_CATEGORY, requestBrand.getCategory())
                .putString(BRAND_SUBCATEGORY, requestBrand.getSubcategory())
                .apply();
    }

    public RequestBrand getCreateBrand() {
        RequestBrand requestBrand = new RequestBrand();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        requestBrand.setTitle(prefs.getString(BRAND_NAME, ""));
        requestBrand.setText(prefs.getString(BRAND_DESCRIPTION, ""));
        requestBrand.setCategory(prefs.getString(BRAND_CATEGORY, ""));
        requestBrand.setSubcategory(prefs.getString(BRAND_SUBCATEGORY, ""));
        return requestBrand;
    }

    public void clearBrand() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .remove(BRAND_NAME)
                .remove(BRAND_DESCRIPTION)
                .remove(BRAND_CATEGORY)
                .remove(BRAND_SUBCATEGORY)
                .apply();
    }

}
