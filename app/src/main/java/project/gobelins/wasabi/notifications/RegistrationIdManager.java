package project.gobelins.wasabi.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import project.gobelins.wasabi.Wasabi;

/**
 * Created by ThomasHiron on 19/04/2015.
 * <p/>
 * Gère la récupération du registration_id
 */
public class RegistrationIdManager
{
    private static final String PROPERTY_APP_VERSION = "1.0";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String GOOGLE_PROJ_ID = "147935468683";
    public static final String MSG_KEY = "message";
    public static final String TITLE_KEY = "title";

    private GoogleCloudMessaging gcm;
    private String regid;
    private Activity activity;
    private Context context;

    public RegistrationIdManager(Activity wasabi)
    {
        activity = wasabi;
        context = wasabi.getApplicationContext();
    }

    /**
     * Récupère le registration_id pour envoyer des notifs
     */
    public void getRegistrationID()
    {
        /* On teste si notifs OK */
        if(checkPlayServices())
        {
            gcm = GoogleCloudMessaging.getInstance(context);
            regid = getRegistrationId();

            if(regid.isEmpty())
                registerInBackground();
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            else
            {
                Log.i(Wasabi.TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Récupère le registration_id dans les préférences
     *
     * @return Le registration_id si existant
     */
    private String getRegistrationId()
    {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if(registrationId.isEmpty())
        {
            Log.i(Wasabi.TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if(registeredVersion != currentVersion)
        {
            Log.i(Wasabi.TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences()
    {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return context.getSharedPreferences(Wasabi.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground()
    {
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
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(GOOGLE_PROJ_ID);
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
                    storeRegistrationId(regid);
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

    /**
     * Envoi le registration_id au serveur
     */
    private void sendRegistrationIdToBackend()
    {
        // TODO
    }

    /**
     * Enregistre le registration_id dans les préférences
     *
     * @param regId Le registration_id
     */
    private void storeRegistrationId(String regId)
    {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion();
        Log.i(Wasabi.TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    /**
     * Retourne la version de l'application
     *
     * @return
     */
    private int getAppVersion()
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
}
