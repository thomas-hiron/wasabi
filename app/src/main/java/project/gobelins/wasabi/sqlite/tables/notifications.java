package project.gobelins.wasabi.sqlite.tables;

import android.net.Uri;

import project.gobelins.wasabi.sqlite.ContentProvider;

/**
 * La table des notifications
 * <p/>
 * Created by ThomasHiron on 27/04/2015.
 */
public class Notifications
{
    public static final String NOTIFICATIONS_ID = "id"; /* L'id */
    public static final String NOTIFICATIONS_DB_ID = "id_db"; /* L'id */
    public static final String NOTIFICATIONS_READ = "read"; /* Si la notif a été lue */
    public static final String NOTIFICATIONS_TYPE = "type"; /* Permet de savoir comment afficher la notif (playlist,...) */
    public static final String NOTIFICATIONS_RECEIVED_DATE = "received_date"; /* La date de réception */

    public static final String TABLE_NOTIFICATIONS = "notifications";

    public static final String URL_NOTIFICATIONS = "content://" + ContentProvider.PROVIDER_NAME + "/" + TABLE_NOTIFICATIONS;
    public static final Uri CONTENT_URI_NOTIFICATIONS = Uri.parse(URL_NOTIFICATIONS);

    public static final String CREATE_TABLE_NOTIFICATIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATIONS + " " +
            "(" +
            NOTIFICATIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NOTIFICATIONS_DB_ID + " INTEGER, " +
            NOTIFICATIONS_READ + " INTEGER," +
            NOTIFICATIONS_TYPE + " INTEGER," +
            NOTIFICATIONS_RECEIVED_DATE + " TEXT" +
            ")";
}