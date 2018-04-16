package com.mgrmobi.brandbeat.ui.widget.brand_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.memory.PoolConfig;
import com.facebook.imagepipeline.memory.PoolFactory;
import com.facebook.imagepipeline.memory.PoolParams;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.mgrmobi.brandbeat.BuildConfig;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.widget.brand_view.utils.DraweeUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class WidgetCoverSwapper extends FrameLayout {
    private static final int NO_INDEX = -1;
    public static final long COVER_CHANGE_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(2);
    private static final long DEFAULT_ANIM_DURATION_MILLIS = 400;
    private final ScalableDraweeView draweeOnBottom;
    private final ScalableDraweeView draweeOnTop;
    private final Paint layerSaturationPaint = new Paint();
    private final AtomicBoolean showInCycle = new AtomicBoolean(true);
    private final AtomicBoolean justOneImage = new AtomicBoolean();
    private ValueAnimator animator;
    private VisibilityChangeCallback visibilityChangeCallback;
    private AnimationCallback animationCallback;
    private List<Pair<Uri, Uri>> itemsToShow;
    private SwapRunnable coverUpdateRunnable;
    private int scheduledPhotoIndex = NO_INDEX;
    private long animDurationMillis = DEFAULT_ANIM_DURATION_MILLIS;
    private boolean shouldUseSaturationWhenSwapping = true;

    public interface VisibilityChangeCallback {
        void onVisibilityChanged(int newVisibility);
    }

    public interface AnimationCallback {
        void onAnimationStart();

        void onAnimationEnd();
    }

    private class SwapRunnable implements Runnable {
        private Pair<Uri, Uri> item;

        public void setItem(Pair<Uri, Uri> item) {
            this.item = item;
        }

        @Override
        public void run() {
            Uri lowResUri = item.second;
            Uri uri = item.first;
            showImage(uri, lowResUri, false, null);
            currentShowingPair = item;
        }
    }

    public WidgetCoverSwapper(Context context) {
        super(context);
        inflate(context, R.layout.widget_event_cover_images, this);
        setId(R.id.id_widget_event_cover_swapper);
        draweeOnBottom = (ScalableDraweeView) findViewById(R.id.id_event_cover_drawee_bottom);
        draweeOnTop = (ScalableDraweeView) findViewById(R.id.id_event_cover_drawee_top);
    }

    public WidgetCoverSwapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.widget_event_cover_images, this);
        setId(R.id.id_widget_event_cover_swapper);
        draweeOnBottom = (ScalableDraweeView) findViewById(R.id.id_event_cover_drawee_bottom);
        draweeOnTop = (ScalableDraweeView) findViewById(R.id.id_event_cover_drawee_top);
    }

    public WidgetCoverSwapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_event_cover_images, this);
        setId(R.id.id_widget_event_cover_swapper);
        draweeOnBottom = (ScalableDraweeView) findViewById(R.id.id_event_cover_drawee_bottom);
        draweeOnTop = (ScalableDraweeView) findViewById(R.id.id_event_cover_drawee_top);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WidgetCoverSwapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.widget_event_cover_images, this);
        setId(R.id.id_widget_event_cover_swapper);
        draweeOnBottom = (ScalableDraweeView) findViewById(R.id.id_event_cover_drawee_bottom);
        draweeOnTop = (ScalableDraweeView) findViewById(R.id.id_event_cover_drawee_top);
    }

    public boolean showImage(@Nullable Uri uri, @Nullable Uri lowResUri, boolean justOnce, @Nullable ControllerListener<ImageInfo> listener) {
        if (isAnimationRunning()) {
            return false;
        }
        this.justOneImage.set(justOnce);
        draweeOnTop.setAlpha(0f);
        draweeOnBottom.setAlpha(1f);
        DisplayMetrics m = getResources().getDisplayMetrics();
        DraweeController controller = DraweeUtils.simpleDraweeController(draweeOnTop, uri, lowResUri, new FrescoControllerListener() {
            @Override
            public void onFailure(String id, Throwable throwable) {
                if (BuildConfig.DEBUG) {
                    imageLoadingError(getContext(), uri, throwable);
                }
                if (listener != null) {
                    listener.onFailure(id, throwable);
                }
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                if (animationCallback != null) {
                    animationCallback.onAnimationStart();
                }
                animateFadeTransition(uri, lowResUri, null);

                if (listener != null) {
                    listener.onFinalImageSet(id, imageInfo, animatable);
                }
            }
        }, m.widthPixels * 2 / 3, m.heightPixels * 2 / 3);
        if (controller != null) {
            draweeOnTop.setController(controller);
            return true;
        }
        return false;
    }

    public boolean showImageJustOnce(@Nullable Uri uri, @Nullable Uri lowResUri, @Nullable ControllerListener<ImageInfo> listener) {
        return showImage(uri, lowResUri, true, listener);
    }

    public boolean showImageJustOnce(@Nullable Uri uri, @Nullable Uri lowResUri, @Nullable Postprocessor postprocessor,
                                     @Nullable ControllerListener<ImageInfo> listener) {
        return showImageJustOnce(uri, lowResUri, postprocessor, listener, true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean showImageJustOnce(@Nullable Uri uri, @Nullable Uri lowResUri, @Nullable Postprocessor postprocessor,
                                     @Nullable ControllerListener<ImageInfo> listener, boolean useFadeTransition) {
        if (isAnimationRunning()) {
            return false;
        }
        this.justOneImage.set(true);
        draweeOnTop.setAlpha(0f);
        draweeOnBottom.setAlpha(0f);
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();

        ImageRequest lowRes = DraweeUtils.simpleRequest(lowResUri);
        if (lowRes != null) {
           // builder.setLowResImageRequest(DraweeUtils.simpleRequest(lowResUri));
        }
        if (uri != null) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .setResizeOptions(DraweeUtils.createResizeOptions(0, 0))
                    .setPostprocessor(postprocessor)
                    .build();
            builder.setImageRequest(request);
        }

  /*      int MaxRequestPerTime = 64;
        SparseIntArray defaultBuckets = new SparseIntArray();
        defaultBuckets.put(16 * ByteConstants.KB, MaxRequestPerTime);
        PoolParams smallByteArrayPoolParams = new PoolParams(
                16 * ByteConstants.KB * MaxRequestPerTime,
                2 * ByteConstants.MB,
                defaultBuckets);
        PoolFactory factory = new PoolFactory(
                PoolConfig.newBuilder()
                        . setSmallByteArrayPoolParams(smallByteArrayPoolParams)
                        .build());

        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(context,)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setDownsampleEnabled(true)
                .setPoolFactory(factory)
                .build();*/
        builder.setCallerContext(draweeOnTop.getContext())
                .setOldController(draweeOnTop.getController())
                .setControllerListener(new FrescoControllerListener() {
                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        if (listener != null) {
                            listener.onFailure(id, throwable);
                        }
                        if (BuildConfig.DEBUG) {
                    //        imageLoadingError(getContext(), uri, throwable);
                        }
                        //Log.e("asd", throwable.getMessage());
                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        if (animationCallback != null) {
                            animationCallback.onAnimationStart();
                        }
                        if (useFadeTransition) {
                            animateFadeTransition(uri, lowResUri, listener);
                        }
                        else {
                            loadImageToBottomView(uri, lowResUri, listener);
                        }
                    }
                });
        draweeOnTop.setController(builder.build());
        return true;
    }

    private void animateFadeTransition(Uri uri, Uri lowResUri, @Nullable ControllerListener<ImageInfo> listener) {
        animator = new ValueAnimator();
        animator.setFloatValues(0, 1);
        animator.addUpdateListener(animation -> {
            float v = (float) animation.getAnimatedValue();
            draweeOnTop.setAlpha(v);
            if (shouldUseSaturationWhenSwapping) {
                updateSaturatedPaint(1 - v);
                draweeOnBottom.setLayerType(LAYER_TYPE_HARDWARE, layerSaturationPaint);

                updateSaturatedPaint(v);
                draweeOnTop.setLayerType(LAYER_TYPE_HARDWARE, layerSaturationPaint);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                loadImageToBottomView(uri, lowResUri, listener);
                draweeOnBottom.setLayerType(LAYER_TYPE_NONE, null);
                draweeOnTop.setLayerType(LAYER_TYPE_NONE, null);
                animator = null;
            }
        });
        draweeOnBottom.setLayerType(LAYER_TYPE_HARDWARE, null);
        draweeOnBottom.setLayerType(LAYER_TYPE_HARDWARE, null);
        animator.setDuration(animDurationMillis);
        animator.start();
    }

    private void loadImageToBottomView(@Nullable Uri uri, @Nullable Uri lowResUri, @Nullable ControllerListener<ImageInfo> listener) {
        DraweeController controller = DraweeUtils.simpleDraweeController(draweeOnBottom, uri, lowResUri, new FrescoControllerListener() {

            @Override
            public void onFailure(String id, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure(id, throwable);
                }
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                draweeOnTop.setAlpha(0f);
                draweeOnBottom.setAlpha(1f);
                if (!justOneImage.get()) {
                    scheduleToUpdateTopPhoto();
                }
                if (animationCallback != null) {
                    animationCallback.onAnimationEnd();
                }

                if (listener != null) {
                    listener.onFinalImageSet(id, imageInfo, animatable);
                }
            }
        });
        if (controller != null) {
            draweeOnBottom.setController(controller);
        }
    }

    public void setVisibilityChangeCallback(@Nullable VisibilityChangeCallback callback) {
        visibilityChangeCallback = callback;
    }

    public void setAnimationCallback(@Nullable AnimationCallback callback) {
        animationCallback = callback;
    }

    public boolean isAnimationRunning() {
        return animator != null && animator.isRunning();
    }

    public void showItems(@Nullable List<Pair<Uri, Uri>> items, boolean showInCycle) {
        itemsToShow = items;
        scheduledPhotoIndex = NO_INDEX;
        if (canSwap()) {
            this.justOneImage.set(false);
            this.showInCycle.set(showInCycle && items != null && items.size() > 1);
            startSwapping();
        }
    }

    private void startSwapping() {
        stopSwapping();
        scheduleToUpdateTopPhoto();
    }

    private void stopSwapping() {
        if (coverUpdateRunnable != null) {
            removeCallbacks(coverUpdateRunnable);
        }
    }

    private Pair<Uri, Uri> currentShowingPair;

    private void scheduleToUpdateTopPhoto() {
        if (canSwap()) {
            if (coverUpdateRunnable == null) {
                coverUpdateRunnable = new SwapRunnable();
            }
            ++scheduledPhotoIndex;
            coverUpdateRunnable.setItem(itemsToShow.get(scheduledPhotoIndex));
            if (scheduledPhotoIndex + 1 >= itemsToShow.size()) {
                scheduledPhotoIndex = NO_INDEX;
                if (showInCycle.get()) {
                    postDelayed(coverUpdateRunnable, getDelay());
                }
            }
            else {
                postDelayed(coverUpdateRunnable, getDelay());
            }
        }
    }

    public void pause() {
        stopSwapping();
        if (scheduledPhotoIndex != NO_INDEX) {
            --scheduledPhotoIndex;
        }
    }

    public void resume() {
        startSwapping();
    }

    @Nullable
    public ScalableDraweeView getBottomView() {
        return draweeOnBottom;
    }

    @Nullable
    public ScalableDraweeView getTopView() {
        return draweeOnTop;
    }

    @Nullable
    public Pair<Uri, Uri> getCurrentVisibleItemLinks() {
        if (itemsToShow == null || itemsToShow.isEmpty()) {
            return null;
        }
        return currentShowingPair;
    }

    public void setUseSaturationWhenSwapping(boolean use) {
        shouldUseSaturationWhenSwapping = use;
    }

    public void setAnimDurationMillis(long duration, TimeUnit timeUnit) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be > 0");
        }
        animDurationMillis = timeUnit.toMillis(duration);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibilityChangeCallback != null) {
            visibilityChangeCallback.onVisibilityChanged(visibility);
        }
        if (visibility == VISIBLE) {
            if (canSwap()) {
                startSwapping();
            }
        }
        else {
            stopSwapping();
        }
    }

    private static void imageLoadingError(Context context, Uri uri, Throwable throwable) {
        Toast.makeText(context, "Can't load image " + uri + ", because of: " + throwable, Toast.LENGTH_SHORT).show();
    }

    private long getDelay() {
        return COVER_CHANGE_DELAY_MILLIS;
    }

    private boolean canSwap() {
        return itemsToShow != null && !itemsToShow.isEmpty();
    }

    private void updateSaturatedPaint(@FloatRange(from = 0, to = 1) float saturationValue) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(saturationValue);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        layerSaturationPaint.setColorFilter(filter);
    }
}
