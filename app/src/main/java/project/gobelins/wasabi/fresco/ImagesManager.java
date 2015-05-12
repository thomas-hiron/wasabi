package project.gobelins.wasabi.fresco;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import project.gobelins.wasabi.entities.Entity;
import project.gobelins.wasabi.entities.Image;
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
     * Récupération des images
     */
    public HashMap<Date, ArrayList<Entity>> getImages()
    {
        Cursor c = mContentResolver.query(Uri.parse(Images.URL_IMAGES), null, null, null, null);

        HashMap<Date, ArrayList<Entity>> images = new HashMap<>();

        if(c.moveToFirst())
        {
            do
            {
                /* Instanciation de la notification */
                Image image = new Image();
                image.setId(c.getInt(c.getColumnIndex(Images.IMAGES_ID)));
                image.setDate(c.getString(c.getColumnIndex(Images.IMAGES_DATE)));
                image.setFileName(c.getString(c.getColumnIndex(Images.IMAGES_FILE_NAME)));
                image.setPoint(c.getString(c.getColumnIndex(Images.IMAGES_POINT)));

                /* Récupération et initialisation si null */
                ArrayList<Entity> dateImages = images.get(image.getDate());
                if(dateImages == null)
                    dateImages = new ArrayList<>();

                /* Ajout du dessin */
                dateImages.add(image);
                images.put(image.getDate(), dateImages);
            }
            while(c.moveToNext());
        }

        return images;
    }

    /**
     * Enregistre une image
     *  @param point    Les coordonnées
     * @param fileName Le fichier
     */
    public int saveImage(Point point, String fileName)
    {
        /* Nouvelles valeurs */
        ContentValues contentValues = new ContentValues(3);
        contentValues.put(Images.IMAGES_DATE, DateFormater.getTodayAsString());
        contentValues.put(Images.IMAGES_POINT, point.toString());
        contentValues.put(Images.IMAGES_FILE_NAME, fileName);

        /* Insertion */
        Uri inserted = mContentResolver.insert(Uri.parse(Images.URL_IMAGES), contentValues);
        long id = ContentUris.parseId(inserted);

        return (int) id;
    }
}