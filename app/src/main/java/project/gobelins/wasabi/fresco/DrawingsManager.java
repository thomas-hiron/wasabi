package project.gobelins.wasabi.fresco;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import project.gobelins.wasabi.entities.Drawing;
import project.gobelins.wasabi.sqlite.tables.Drawings;

/**
 * Permet de récupérer tous les dessins
 * <p/>
 * Created by ThomasHiron on 12/05/2015.
 */
public class DrawingsManager
{
    private ContentResolver mContentResolver;

    public DrawingsManager(ContentResolver contentResolver)
    {
        mContentResolver = contentResolver;
    }

    /**
     * Récupération des dessins
     */
    public HashMap<Date, ArrayList<Drawing>> getDrawings()
    {
        Cursor c = mContentResolver.query(Uri.parse(Drawings.URL_DRAWINGS), null, null, null, null);

        HashMap<Date, ArrayList<Drawing>> drawings = new HashMap<>();

        if(c.moveToFirst())
        {
            do
            {
                /* Instanciation de la notification */
                Drawing drawing = new Drawing();
                drawing.setId(c.getInt(c.getColumnIndex(Drawings.DRAWINGS_ID)));
                drawing.setDate(c.getString(c.getColumnIndex(Drawings.DRAWINGS_DATE)));
                drawing.setColor(c.getInt(c.getColumnIndex(Drawings.DRAWINGS_COLOR)));
                drawing.setPoints(c.getString(c.getColumnIndex(Drawings.DRAWINGS_POINTS)));

                /* Récupération et initialisation si null */
                ArrayList<Drawing> dateDrawings = drawings.get(drawing.getDate());
                if(dateDrawings == null)
                    dateDrawings = new ArrayList<>();

                /* Ajout du dessin */
                dateDrawings.add(drawing);
                drawings.put(drawing.getDate(), dateDrawings);
            }
            while(c.moveToNext());
        }

        return drawings;
    }
}