package project.gobelins.wasabi.sqlite.tables;

import android.net.Uri;

import project.gobelins.wasabi.sqlite.ContentProvider;

/**
 * La table des dessisn
 * <p/>
 * Created by ThomasHiron on 27/04/2015.
 */
public class Images
{
    public static final String IMAGES_ID = "id"; /* L'id */
    public static final String IMAGES_DATE = "date"; /* La date du dessin */
    public static final String IMAGES_POINT = "point"; /* Les coordonnées */
    public static final String IMAGES_FILE_NAME = "file_name"; /* Les coordonnées */

    public static final String TABLE_IMAGES = "images";

    public static final String URL_IMAGES = "content://" + ContentProvider.PROVIDER_NAME + "/" + TABLE_IMAGES;
    public static final Uri CONTENT_URI_IMAGES = Uri.parse(URL_IMAGES);

    public static final String CREATE_TABLE_IMAGES = "CREATE TABLE IF NOT EXISTS " + TABLE_IMAGES + " " +
            "(" +
            IMAGES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            IMAGES_DATE + " TEXT," +
            IMAGES_POINT + " TEXT," +
            IMAGES_FILE_NAME + " TEXT" +
            ")";
}