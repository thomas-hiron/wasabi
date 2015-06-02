package project.gobelins.wasabi.notifications.receiver;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.RegistrationIdManager;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.sqlite.tables.Notifications;
import project.gobelins.wasabi.utils.DateFormater;

public class GCMNotificationIntentService extends IntentService
{
    // Sets an ID for the notification, so it can be updated
    public static final int notifyID = 9001;

    public GCMNotificationIntentService()
    {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if(!extras.isEmpty() && !extras.getString(Wasabi.REQUEST_ID, "0").equals("0"))
        {
            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
                sendNotification("Error", extras.toString());
            else if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
                sendNotification("Deleted messages on server", extras.toString());
            else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
                sendNotification(extras.getString(RegistrationIdManager.TITLE_KEY), extras.getString(RegistrationIdManager.MSG_KEY));
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);

        /* Les valeurs */
        String requestIdString = extras.getString(Wasabi.REQUEST_ID);
        if(requestIdString != null && !requestIdString.equals("0"))
        {
            int requestId = Integer.parseInt(requestIdString);
            ContentValues contentValues = new ContentValues(4);
            contentValues.put(Notifications.NOTIFICATIONS_ID, requestId);
            contentValues.put(Notifications.NOTIFICATIONS_READ, 0);
            contentValues.put(Notifications.NOTIFICATIONS_TYPE, Integer.parseInt(extras.getString(Wasabi.REQUEST_TYPE)));
            contentValues.put(Notifications.NOTIFICATIONS_RECEIVED_DATE, DateFormater.getTodayAsString());

            /* Suppression de toutes les notifs pour la soutenance */
            getContentResolver().delete(Uri.parse(Notifications.URL_NOTIFICATIONS), null, null);
            /* Insertion dans la table */
            getContentResolver().insert(Uri.parse(Notifications.URL_NOTIFICATIONS), contentValues);

            /* Ajout d'un marqueur pour jouer l'animation une seule fois */
            SharedPreferences sharedPreferences = getSharedPreferences(Wasabi.class.getSimpleName(), MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean(Wasabi.CUSTOM_ANIM_NOT_PLAYED, true);
            edit.apply();
        }
        /* Dessin, on met dans les sharedPref */
        else if(requestIdString != null && requestIdString.equals("0"))
        {
            SharedPreferences sharedPreferences = getSharedPreferences(Wasabi.class.getSimpleName(), MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean(Wasabi.ACCOMPLICE_DRAWED, true);
            edit.apply();
        }
    }

    /**
     * Envoie la notification
     *
     * @param title Le titre
     * @param msg   Le message
     */
    private void sendNotification(String title, String msg)
    {
        /* On passe des variables au clic sur la notification */
        Intent resultIntent = new Intent(this, Wasabi.class);
        resultIntent.putExtra("msg", msg);
        resultIntent.putExtra("title", title);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
}
