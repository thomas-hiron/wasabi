package project.gobelins.wasabi.sqlite.tables;

import android.net.Uri;

import project.gobelins.wasabi.sqlite.ContentProvider;

/**
 * La table des dessisn
 * <p/>
 * Created by ThomasHiron on 27/04/2015.
 */
public class Drawings
{
    public static final String DRAWINGS_ID = "id"; /* L'id */
    public static final String DRAWINGS_DATE = "date"; /* La date du dessin */
    public static final String DRAWINGS_POINTS = "points"; /* Tous les points */
    public static final String DRAWINGS_COLOR = "color"; /* La date de réception */

    public static final String TABLE_DRAWINGS = "drawings";

    public static final String URL_DRAWINGS = "content://" + ContentProvider.PROVIDER_NAME + "/" + TABLE_DRAWINGS;
    public static final Uri CONTENT_URI_DRAWINGS = Uri.parse(URL_DRAWINGS);

    public static final String CREATE_TABLE_DRAWINGS = "CREATE TABLE IF NOT EXISTS " + TABLE_DRAWINGS + " " +
            "(" +
            DRAWINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DRAWINGS_DATE + " TEXT," +
            DRAWINGS_POINTS + " TEXT," +
            DRAWINGS_COLOR + " INTEGER" +
            ")";
}