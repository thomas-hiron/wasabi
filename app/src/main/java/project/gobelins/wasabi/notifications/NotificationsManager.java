package project.gobelins.wasabi.notifications;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.sqlite.tables.Notifications;
import project.gobelins.wasabi.utils.DateFormater;

/**
 * Created by ThomasHiron on 28/04/2015.
 */
public class NotificationsManager extends ArrayList<Notification>
{
    private final ContentResolver mContentResolver;

    public NotificationsManager(ContentResolver contentResolver)
    {
        mContentResolver = contentResolver;
    }

    /**
     * Marque comme lu la notification
     *
     * @param notification
     */
    public void markRead(Notification notification)
    {
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
        String date = DateFormater.getTodayAsString();

        /* Tous les messages non lus ou date égale à aujourd'hui */
        String readCondition = Notifications.NOTIFICATIONS_READ + " = 0 OR " +
                Notifications.NOTIFICATIONS_RECEIVED_DATE + " = '" + date + "'";
        String sortOrder = Notifications.NOTIFICATIONS_ID;

        Cursor c = mContentResolver.query(Uri.parse(Notifications.URL_NOTIFICATIONS), null, readCondition, null, sortOrder);

        if(c.moveToFirst())
        {
            do
            {
                /* Instanciation de la notification */
                Notification notification = new Notification();
                notification.setId(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_ID)));
                notification.setIdDb(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_DB_ID)));
                notification.setRead(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_READ)));
                notification.setType(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_TYPE)));
                notification.setDate(c.getString(c.getColumnIndex(Notifications.NOTIFICATIONS_RECEIVED_DATE)));

                /* Ajout de la notif au manager */
                add(notification);
            }
            while(c.moveToNext());
        }

        c.close();

        return this;
    }

    /**
     * @return La dernière notification
     */
    public Notification getLast()
    {
        /* On récupère toutes les notifs */
        if(size() == 0)
            getNotifications();

        return size() > 0 ? get(size() - 1) : null;
    }
}
