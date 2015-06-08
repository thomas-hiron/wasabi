package project.gobelins.wasabi.sqlite.tables;

import android.net.Uri;

import project.gobelins.wasabi.sqlite.ContentProvider;

/**
 * La table des dessisn
 * <p/>
 * Created by ThomasHiron on 27/04/2015.
 */
public class AccompliceDrawing
{
    public static final String ACCOMPLICE_DRAWINGS_ID = "id"; /* L'id */
    public static final String ACCOMPLICE_DRAWINGS_POINTS = "points"; /* Tous les points */
    public static final String ACCOMPLICE_DRAWINGS_COLOR = "color"; /* La date de réception */

    public static final String TABLE_ACCOMPLICE_DRAWINGS = "accomplice_drawings";

    public static final String URL_ACCOMPLICE_DRAWINGS = "content://" + ContentProvider.PROVIDER_NAME + "/" + TABLE_ACCOMPLICE_DRAWINGS;
    public static final Uri CONTENT_URI_ACCOMPLICE_DRAWINGS = Uri.parse(URL_ACCOMPLICE_DRAWINGS);

    public static final String CREATE_TABLE_ACCOMPLICE_DRAWINGS = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCOMPLICE_DRAWINGS + " " +
            "(" +
            ACCOMPLICE_DRAWINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ACCOMPLICE_DRAWINGS_POINTS + " TEXT," +
            ACCOMPLICE_DRAWINGS_COLOR + " INTEGER" +
            ")";
}