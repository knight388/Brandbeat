package com.mgrmobi.sdk.social.base.model;

/**
 * @author Valentin S. Bolkonsky.
 */
public class ShareMediaFile {

    private String filename;
    private ShareMediaType type;
    private String mediaPath;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ShareMediaType getType() {
        return type;
    }

    public void setType(ShareMediaType type) {
        this.type = type;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    @Override
    public String toString() {
        return "ShareMediaFile{" +
                "filename=" + filename +
                ", mediaPath=" + mediaPath +
                ", type=" + type +
                "}";
    }

    public enum ShareMediaType{

        VIDEO, IMAGE
    }
}
