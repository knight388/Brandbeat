package com.mgrmobi.sdk.social.facebook.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.Profile;
import com.mgrmobi.sdk.social.android.model.SocialUser;
import com.mgrmobi.sdk.social.android.model.UserGender;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Valentin S. Bolkonsky.
 */
public class FacebookUtils {

    private FacebookUtils() {

    }

    public static void getFacebookHash(final Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {
            //ignore
        }
    }

    public static SocialUser convert(Profile profile) {
        return new SocialUser(profile.getId(), profile.getFirstName(), profile.getMiddleName(), profile.getLastName(), profile.getName(), String.valueOf(profile.getLinkUri()),
                null, null, String.valueOf(profile.getProfilePictureUri(640, 640)), null, null, null);
    }

    public static SocialUser fromFacebook(final JSONObject jsonObject, final Profile profile) {
        final SocialUser socialUser = new SocialUser();
        try {
            if (jsonObject.has("id")) {
                socialUser.setId(jsonObject.getString("id"));
            }
            if (jsonObject.has("first_name")) {
                socialUser.setFirstName(jsonObject.getString("first_name"));
            }
            if (jsonObject.has("name")) {
                socialUser.setName(jsonObject.getString("name"));
            }
            if (jsonObject.has("last_name")) {
                socialUser.setLastName(jsonObject.getString("last_name"));
            }
            if (jsonObject.has("birthday")) {
                /*"04/24/1974"*/
                final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    final String bdayString = jsonObject.getString("birthday");
                    socialUser.setBirthday(dateFormat.parse(bdayString));
                } catch (final ParseException ignored) {

                }
            }
            if (jsonObject.has("email")) {
                socialUser.setEmail(jsonObject.getString("email"));
            }
            if (jsonObject.has("gender")) {
                final String gender = jsonObject.getString("gender");
                if (gender.equals("male")) {
                    socialUser.setGender(UserGender.MALE);
                } else {
                    socialUser.setGender(UserGender.FEMALE);
                }
            }
            if (jsonObject.has("bio")) {
                final String bio = jsonObject.getString("bio");
                socialUser.setDescription(bio);
            }
            if (profile != null) {
                socialUser.setPicture(String.valueOf(profile.getProfilePictureUri(640, 640)));
                socialUser.setLink(String.valueOf(profile.getLinkUri()));
            }
        } catch (JSONException ignored) {

        }
        return socialUser;
    }

}
