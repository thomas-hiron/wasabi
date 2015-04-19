package project.gobelins.wasabi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class Wasabi extends Activity
{
    private GoogleCloudMessaging gcm;
    private String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wasabi);

        if(checkPlayServices())
        {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(this);

            Log.v("test", "RegId : " + regid);

            if(regid.isEmpty())
            {
                registerInBackground();
            }
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        AsyncTask execute = new AsyncTask()
        {

            @Override
            protected String doInBackground(Object[] objects)
            {
                String msg = "";
                try
                {
                    if(gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(getApplicationContext(), regid);
                }
                catch(IOException ex)
                {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }
        };
        execute.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("test", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("registration_id", regId);
        editor.putInt("1.0", appVersion);
        editor.apply();
    }

    private String getRegistrationId(Context context)
    {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString("registration_id", "");
        if(registrationId.isEmpty())
        {
            Log.i("test", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt("1.0", Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion != currentVersion)
        {
            Log.i("test", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context)
    {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(Wasabi.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            }
            else
            {
                Log.i("test", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && android.os.Build.VERSION.SDK_INT >= 19) // Mode immersif
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}