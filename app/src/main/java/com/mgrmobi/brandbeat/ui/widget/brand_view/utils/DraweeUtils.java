package com.mgrmobi.brandbeat.ui.widget.brand_view.utils;

import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class DraweeUtils {

    @Nullable
    public static DraweeController simpleDraweeController(SimpleDraweeView drawee, Uri uri, @Nullable Uri lowResUri) {
        ImageRequest request = simpleRequest(uri);
        if (request == null) {
            return null;
        }
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setOldController(drawee.getController())
                .setCallerContext(drawee.getContext())
                .setImageRequest(request);
        ImageRequest lowResRequest = simpleRequest(lowResUri);
        if (lowResRequest != null) {
            builder.setLowResImageRequest(lowResRequest);
        }
        return builder.build();
    }

    @Nullable
    public static DraweeController simpleDraweeController(SimpleDraweeView drawee, Uri uri, @Nullable Uri lowResUri,
                                                          @Nullable ControllerListener<ImageInfo> listener) {
        if (listener == null) {
            return simpleDraweeController(drawee, uri, lowResUri);
        }
        ImageRequest request = simpleRequest(uri);
        if (request == null) {
            return null;
        }
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setOldController(drawee.getController())
                .setCallerContext(drawee.getContext())
                .setImageRequest(request);
        ImageRequest lowResRequest = simpleRequest(lowResUri);
        if (lowResRequest != null) {
            builder.setLowResImageRequest(lowResRequest);
        }
        builder.setControllerListener(listener);
        return builder.build();
    }

    @Nullable
    public static DraweeController simpleDraweeController(SimpleDraweeView drawee, Uri uri, @Nullable Uri lowResUri,
                                                          @Nullable ControllerListener<ImageInfo> listener, int resizeX, int resizeY) {
        if (listener == null) {
            return simpleDraweeController(drawee, uri, lowResUri);
        }
        ImageRequest request = simpleRequest(uri, resizeX, resizeY);
        if (request == null) {
            return null;
        }
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setOldController(drawee.getController())
                .setCallerContext(drawee.getContext())
                .setImageRequest(request);
        ImageRequest lowResRequest = simpleRequest(lowResUri);
        if (lowResRequest != null) {
            builder.setLowResImageRequest(lowResRequest);
        }
        builder.setControllerListener(listener);
        return builder.build();
    }

    @Nullable
    public static ImageRequest simpleRequest(@Nullable Uri uri) {
        if (uri == null) {
            return null;
        }
        return ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
    }

    @Nullable
    public static ImageRequest simpleRequest(@Nullable Uri uri, int resizeX, int resizeY) {
        if (uri == null) {
            return null;
        }
        return ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions(resizeX, resizeY))
                .build();
    }

    @NonNull
    public static ResizeOptions createResizeOptions(int resizeX, int resizeY) {
        if (resizeX == 0 && resizeY == 0) {
            DisplayMetrics m = Resources.getSystem().getDisplayMetrics();
            return new ResizeOptions(m.widthPixels * 2 / 3, m.heightPixels * 2 / 3);
        }
        return new ResizeOptions(resizeX, resizeY);
    }
}
