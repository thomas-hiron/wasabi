package project.gobelins.wasabi;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.sqlite.tables.Notifications;

/**
 * Created by ThomasHiron on 28/04/2015.
 */
public class NotificationsManager extends ArrayList<Notification>
{
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    private final ContentResolver mContentResolver;

    public NotificationsManager(ContentResolver contentResolver)
    {
        mContentResolver = contentResolver;
    }

    /**
     * Marque comme lu la notification
     *
     * @param i
     */
    public void markRead(int i)
    {
        /* Récupération de la notification */
        Notification notification = get(i);

        /* Insertion de la nouvelle valeur modifiée */
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(Notifications.NOTIFICATIONS_READ, 1);

        /* La condition d'update */
        String condition = Notifications.NOTIFICATIONS_ID + " = " + notification.getId();

        /* Exécution de la requête */
        mContentResolver.update(Uri.parse(Notifications.URL_NOTIFICATIONS), contentValues, condition, null);
    }

    /**
     * Récupère toutes les notifications
     *
     * @return Un tableau de notifications
     */
    public ArrayList<Notification> getNotifications()
    {
        /* La date du jour */
        DateFormat dateFormat = new SimpleDateFormat(NotificationsManager.DATE_FORMAT);
        Date date = new Date();

        /* Tous les messages non lus ou date égale à aujourd'hui */
        String readCondition = Notifications.NOTIFICATIONS_READ + " = 0 OR " +
                Notifications.NOTIFICATIONS_RECEIVED_DATE + " = '" + dateFormat.format(date) + "'";
        String sortOrder = Notifications.NOTIFICATIONS_ID + " DESC";

        Cursor c = mContentResolver.query(Uri.parse(Notifications.URL_NOTIFICATIONS), null, readCondition, null, sortOrder);

        if(c.moveToFirst())
        {
            do
            {
                /* Instanciation de la notification */
                Notification notification = new Notification();
                notification.setId(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_ID)));
                notification.setRead(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_READ)));
                notification.setType(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_TYPE)));
                notification.setDate(c.getString(c.getColumnIndex(Notifications.NOTIFICATIONS_RECEIVED_DATE)));

                /* Ajout de la notif au manager */
                add(notification);
            }
            while(c.moveToNext());
        }

        return this;
    }
}
