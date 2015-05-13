package project.gobelins.wasabi.sqlite.tables;

import android.net.Uri;

import project.gobelins.wasabi.sqlite.ContentProvider;

/**
 * La table des dessisn
 * <p/>
 * Created by ThomasHiron on 27/04/2015.
 */
public class Sounds
{
    public static final String SOUNDS_ID = "id"; /* L'id */
    public static final String SOUNDS_DATE = "date"; /* La date du dessin */
    public static final String SOUNDS_POINT = "point"; /* Les coordonnées */
    public static final String SOUNDS_FILE_NAME = "file_name"; /* Les coordonnées */

    public static final String TABLE_SOUNDS = "sounds";

    public static final String URL_SOUNDS = "content://" + ContentProvider.PROVIDER_NAME + "/" + TABLE_SOUNDS;
    public static final Uri CONTENT_URI_SOUNDS = Uri.parse(URL_SOUNDS);

    public static final String CREATE_TABLE_SOUNDS = "CREATE TABLE IF NOT EXISTS " + TABLE_SOUNDS + " " +
            "(" +
            SOUNDS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SOUNDS_DATE + " TEXT," +
            SOUNDS_POINT + " TEXT," +
            SOUNDS_FILE_NAME + " TEXT" +
            ")";
}