package com.mgrmobi.brandbeat.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseNotification;
import com.mgrmobi.brandbeat.ui.activity.AnotherUserProfileActivity;
import com.mgrmobi.brandbeat.ui.activity.NavigationActivity;
import com.mgrmobi.brandbeat.ui.activity.NewBrandActivity;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class BrandBeatGcmListenerService extends GcmListenerService {

    private static final String TAG = "BrandBeatGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        //Log.e("adspikhfjae;rj", message);
        Gson gson = new Gson();
        ResponseNotification response = gson.fromJson(message, ResponseNotification.class);

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        message = response.getText();
        sendNotification(response);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param response GCM message received.
     */
    private void sendNotification(ResponseNotification response) {

        Intent intent = null;
        switch (response.getEnum()) {
            case FOLLOW:
                intent = AnotherUserProfileActivity.createIntent(getBaseContext(), response.getUserId());
                break;
            case ACHIEVEMENT:
                intent = new Intent(this, NavigationActivity.class);
                break;
            case ACCEPTED_BRAND:
                intent = NewBrandPagerActivtiy.createIntent(response.getBrand().getId(), response.getBrand().getSubCategoryId(), getBaseContext());
                break;
            case ADVERTSING:
                intent = new Intent(this, NavigationActivity.class);
                break;
            case ANNOUNCEMENTS:
                intent = new Intent(this, NavigationActivity.class);
                break;
            case COMMENT_REVIEW:
                intent = ReviewActivity.createIntent(response.getReviewId(), "", getBaseContext());
                break;
            case LIKE_COMMENT:
                intent = ReviewActivity.createIntent(response.getReviewId(), "", getBaseContext());
                break;
            case LIKE_REVIEW:
                intent = ReviewActivity.createIntent(response.getReviewId(), "", getBaseContext());
                break;
            case REJECTED_BRAND:
                intent = new Intent(this, NavigationActivity.class);
                break;
            case REPLIED:
                intent = new Intent(this, NavigationActivity.class);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(BrandBeatGcmListenerService.this)
                        .setSmallIcon(R.drawable.bb_statisbar_icon)
                        //          .setColor(R.color.color_accent)
                        .setLargeIcon(bitmap)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(response.getText())
                        //          .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon512);
                NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(BrandBeatGcmListenerService.this)
                        .setSmallIcon(R.drawable.bb_statisbar_icon)
                        //          .setColor(R.color.color_accent)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(response.getText())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (response.getPhotoUrl(BrandBeatGcmListenerService.this) == null) {
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon512);
                    NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(BrandBeatGcmListenerService.this)
                            .setSmallIcon(R.drawable.bb_statisbar_icon)
                            //          .setColor(R.color.color_accent)
                            .setLargeIcon(largeIcon)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(response.getText())
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, notificationBuilder.build());
                }
                else
                    Picasso.with(BrandBeatGcmListenerService.this).load(response.getPhotoUrl(BrandBeatGcmListenerService.this)).into(target);
            }
        });
    }
}