package project.gobelins.wasabi.fresco;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.sqlite.tables.Images;
import project.gobelins.wasabi.utils.DateFormater;

/**
 * Permet de récupérer tous les dessins
 * <p/>
 * Created by ThomasHiron on 12/05/2015.
 */
public class ImagesManager
{
    private ContentResolver mContentResolver;

    public ImagesManager(ContentResolver contentResolver)
    {
        mContentResolver = contentResolver;
    }

    /**
     * Enregistre une image
     *
     * @param point    Les coordonnées
     * @param fileName Le fichier
     */
    public void saveImage(Point point, String fileName)
    {
        /* Nouvelles valeurs */
        ContentValues contentValues = new ContentValues(3);
        contentValues.put(Images.IMAGES_DATE, DateFormater.getTodayAsString());
        contentValues.put(Images.IMAGES_POINT, point.toString());
        contentValues.put(Images.IMAGES_FILE_NAME, fileName);

        /* Insertion */
        mContentResolver.insert(Uri.parse(Images.URL_IMAGES), contentValues);
    }
}