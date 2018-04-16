package com.mgrmobi.brandbeat.network.responce;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;

import java.io.Serializable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseAchievement implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String image;
    @SerializedName("id")
    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getImage(PhotoSize photoSize, Context context) {
        //if(image == null || image.equals("")) return image;
        //return image + context.getString(photoSize.value);
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
