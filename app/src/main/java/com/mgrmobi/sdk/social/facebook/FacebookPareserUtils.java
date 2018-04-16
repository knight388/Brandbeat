package com.mgrmobi.sdk.social.facebook;

import com.google.gson.Gson;
import org.json.JSONObject;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class FacebookPareserUtils {
    public static FaceBookProfileObject parseObject(JSONObject json) {
        Gson gson = new Gson();
        return gson.fromJson(String.valueOf(json), FaceBookProfileObject.class);
    }
}
