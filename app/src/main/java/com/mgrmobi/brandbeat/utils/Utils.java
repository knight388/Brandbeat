package com.mgrmobi.brandbeat.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.ui.callbacks.PermissionCallBack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class Utils {
    public static final SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat formatTextDate = new SimpleDateFormat("dd MMMM, yyyy");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    public static final int RATING_BAR_DELETE_VALUE = 5;
    public static final int PAGE_SIZE = 20;

    public static String getHardwareId() {
        String hardwareId = "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;

        return hardwareId;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static ImageRequest createImageRequest(int width, int height, Uri uri) {
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(false)
                .build();
        return request;
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return getString(R.string.just_now);
        }
        else if (diff < 2 * MINUTE_MILLIS) {
            return getString(R.string.a_minute_ago);
        }
        else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " " + getString(R.string.minutes_ago);
        }
        else if (diff < 90 * MINUTE_MILLIS) {
            return getString(R.string.an_hour_ago);
        }
        else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " " + getString(R.string.hours_ago);
        }
        else if (diff < 48 * HOUR_MILLIS) {
            return getString(R.string.yesterday);
        }
        else {
            Date date = new Date();
            date.setTime(time);
            return dateFormat.format(date);
        }
    }

    public static String getString(int id) {
        return BrandBeatApplication.getInstance().getResources().getString(id);
    }

    public static void showAlertDialog(Context context, DialogInterface.OnClickListener onOkClick,
                                       DialogInterface.OnClickListener onCancelClick, String title,
                                       String message, boolean isOkShow, boolean isCancelClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        if (isOkShow)
            builder.setPositiveButton(context.getString(R.string.ok), onOkClick);
        if (isCancelClick)
            builder.setNegativeButton(context.getString(R.string.cancel), onCancelClick);
        try {
            builder.show();
        } catch (WindowManager.BadTokenException e) {
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        if (context == null) {
            return dp;
        }
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static String getVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = BrandBeatApplication.getInstance().getBaseContext().
                    getPackageManager().getPackageInfo(BrandBeatApplication.getInstance().getBaseContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionName;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for(char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            }
            else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static void checkLocationOlderVersion(Activity context, PermissionCallBack permissionCallBack) {
        LocationManager lm = null;
        boolean gps_enabled = false, network_enabled = false;
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        AlertDialog.Builder dialog;
        if (!gps_enabled && !network_enabled) {
            dialog = new AlertDialog.Builder(context);
            dialog.setMessage(getString(R.string.use_location));
            dialog.setPositiveButton(getString(R.string.ok), (paramDialogInterface, paramInt) -> {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivityForResult(myIntent, 0);
             //   permissionCallBack.onPermissionCallBack();
            });
            dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    permissionCallBack.onPermissionCallBack();
                }
            });
            dialog.show();
        }
        else
            permissionCallBack.onPermissionCallBack();
    }

    public static void getPermission(PermissionCallBack permissionCallBack, Activity activity) {
//dragon        RxResult.checkPermissions(activity,
//                new PermissionInfo(Manifest.permission.ACCESS_COARSE_LOCATION,
//                        getString(R.string.warning), getString(R.string.use_location),
//                        getString(R.string.ok), getString(R.string.cancel)))
//                .subscribe(permission -> permissionCallBack.onPermissionCallBack());
    }

    public static Bitmap loadImageFromResurce(Intent data, Context context) throws FileNotFoundException, IOException {
        Bitmap bitmap;
        Uri selectedImage = data.getData();
        if (selectedImage != null) {
            int orientation;
            ExifInterface exif = new ExifInterface(selectedImage.getPath());
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Matrix matrix = new Matrix();
// rotate the Bitmap (there a problem with exif so we'll query the mediaStore for orientation
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                orientation = cursor.getInt(0);
                matrix.preRotate(orientation);
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            InputStream imageStream = null;
            imageStream = context.getContentResolver().openInputStream(selectedImage);
            BitmapFactory.decodeStream(imageStream, null, options);
            int width = options.outWidth;
            int height = options.outHeight;
            if ((width + height) / (750 + 750) < 1)
                options.inSampleSize = 1;
            else
                options.inSampleSize = (width + height) / (750 + 750);

            options.inJustDecodeBounds = false;
            imageStream = context.getContentResolver().openInputStream(selectedImage);
            bitmap = BitmapFactory.decodeStream(imageStream, null, options);

            if ((orientation != 0)) {
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                Bitmap rotatedBitmap;
                rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                return rotatedBitmap;
            }
        }
        else {
            bitmap = (Bitmap) data.getExtras().get("data");
        }

        return bitmap;
    }
}
