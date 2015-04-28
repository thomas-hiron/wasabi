package project.gobelins.wasabi;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.sqlite.tables.Notifications;

/**
 * Created by ThomasHiron on 28/04/2015.
 */
public class NotificationsManager extends ArrayList<Notification>
{
    public final static String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Marque comme lu la notification
     * @param wasabi
     * @param i
     */
    public void markRead(Wasabi wasabi, int i)
    {
        /* Récupération de la notification */
        Notification notification = get(i);

        ContentResolver contentResolver = wasabi.getContentResolver();

        /* Insertion de la nouvelle valeur modifiée */
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(Notifications.NOTIFICATIONS_READ, 1);

        /* La condition d'update */
        String condition = Notifications.NOTIFICATIONS_ID + " = " + notification.getId();

        /* Exécution de la requête */
        contentResolver.update(Uri.parse(Notifications.URL_NOTIFICATIONS), contentValues, condition, null);
    }
}
