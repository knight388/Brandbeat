package com.mgrmobi.brandbeat.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.gcm.QuickstartPreferences;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.mgrmobi.brandbeat.R.drawable.location;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SplashActivity extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.mgrmobi.brandbeat", PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.REGISTRATION_COMPLETE, false);
            }
        };
        registerReceiver();
        //finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestAllPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestAllPermissions() {
//        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentapiVersion < Build.VERSION_CODES.M) {
            Utils.checkLocationOlderVersion(SplashActivity.this, () -> {
                startActivity();
            });
            return;
//        }

//        PermissionInfo location = new PermissionInfo(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                getString(R.string.warning), getString(R.string.use_location),
//                getString(R.string.ok), getString(R.string.cancel));
//        PermissionInfo getAccounts = new PermissionInfo(
//                Manifest.permission.GET_ACCOUNTS,
//                getString(R.string.warning), getString(R.string.use_account),
//                getString(R.string.ok), getString(R.string.cancel));
//        PermissionInfo useStorage = new PermissionInfo(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                getString(R.string.warning), getString(R.string.use_storage),
//                getString(R.string.ok), getString(R.string.cancel));
//        RxResult.checkPermissions(this, location)
//                .flatMap(item -> RxResult.checkPermissions(this, getAccounts))
//                .flatMap(item -> RxResult.checkPermissions(this, useStorage))
//                .subscribe(permission -> {
//                    startActivity();
//                });
    }

    private void startActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        UserDataUtils userDataUtils = new UserDataUtils(this);
        if (userDataUtils.getUserId() != null && !userDataUtils.getUserId().equals("")) {
            finish();
            startActivity(NavigationActivity.createIntent(this));
        }
        else {
            finish();
            startActivity(LoginActivity.createIntent(this));
        }
    }

    private void registerReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.SENT_TOKEN_TO_SERVER));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startActivity();
    }
}
