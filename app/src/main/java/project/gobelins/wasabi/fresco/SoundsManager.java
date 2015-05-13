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
import project.gobelins.wasabi.entities.Sound;
import project.gobelins.wasabi.sqlite.tables.Sounds;
import project.gobelins.wasabi.utils.DateFormater;

/**
 * Created by ThomasHiron on 13/05/2015.
 */
public class SoundsManager
{
    private ContentResolver mContentResolver;

    public SoundsManager(ContentResolver contentResolver)
    {
        mContentResolver = contentResolver;
    }

    public HashMap<Date, ArrayList<Entity>> getSounds()
    {
        Cursor c = mContentResolver.query(Uri.parse(Sounds.URL_SOUNDS), null, null, null, null);

        HashMap<Date, ArrayList<Entity>> sounds = new HashMap<>();

        if(c.moveToFirst())
        {
            do
            {
                /* Instanciation de la notification */
                Sound sound = new Sound();
                sound.setId(c.getInt(c.getColumnIndex(Sounds.SOUNDS_ID)));
                sound.setDate(c.getString(c.getColumnIndex(Sounds.SOUNDS_DATE)));
                sound.setFileName(c.getString(c.getColumnIndex(Sounds.SOUNDS_FILE_NAME)));
                sound.setPoint(c.getString(c.getColumnIndex(Sounds.SOUNDS_POINT)));

                /* Récupération et initialisation si null */
                ArrayList<Entity> dateSounds = sounds.get(sound.getDate());
                if(dateSounds == null)
                    dateSounds = new ArrayList<>();

                /* Ajout du dessin */
                dateSounds.add(sound);
                sounds.put(sound.getDate(), dateSounds);
            }
            while(c.moveToNext());
        }

        return sounds;
    }

    /**
     * Enregistre un son
     *
     * @param fileName Le fichier
     * @return L'id inséré
     */
    public int saveSound(String fileName)
    {
        /* Nouvelles valeurs */
        ContentValues contentValues = new ContentValues(3);
        contentValues.put(Sounds.SOUNDS_DATE, DateFormater.getTodayAsString());
        contentValues.put(Sounds.SOUNDS_POINT, (byte[]) null);
        contentValues.put(Sounds.SOUNDS_FILE_NAME, fileName);

        /* Insertion */
        Uri inserted = mContentResolver.insert(Uri.parse(Sounds.URL_SOUNDS), contentValues);
        long id = ContentUris.parseId(inserted);

        return (int) id;
    }
}